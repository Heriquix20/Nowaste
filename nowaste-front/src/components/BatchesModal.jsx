import { useState, useEffect } from "react";
import { api } from "../pages/services/api.js";
import Swal from 'sweetalert2';

export default function BatchesModal({ inventoryId, productId, productName, onClose }) {
    const [batches, setBatches] = useState([]);
    const [editingId, setEditingId] = useState(null);

    const hoje = new Date().toISOString().split("T")[0];

    const [batchForm, setBatchForm] = useState({
        supplierBatchCode: "",
        quantity: "",
        expirationDate: ""
    });

    useEffect(() => {
        if (productId && inventoryId) carregarLotes();
    }, [productId, inventoryId]);

    async function carregarLotes() {
        try {
            const response = await api.get(`/inventories/${inventoryId}/products/${productId}/batches`);
            setBatches(response.data);
        } catch (error) {
            console.error("Erro ao carregar lotes:", error);
        }
    }

    function prepararEdicao(batch) {
        setBatchForm({
            supplierBatchCode: batch.supplierBatchCode || batch.code || batch.batchCode || "",
            quantity: batch.quantity,
            expirationDate: batch.expirationDate ? batch.expirationDate.split("T")[0] : ""
        });
        setEditingId(batch.id);
    }

    function cancelarEdicao() {
        setBatchForm({ supplierBatchCode: "", quantity: "", expirationDate: "" });
        setEditingId(null);
    }

    async function salvarLote(e) {
        e.preventDefault();
        try {
            const dadosLote = {
                code: batchForm.supplierBatchCode,
                batchCode: batchForm.supplierBatchCode,
                supplierBatchCode: batchForm.supplierBatchCode,
                quantity: Number(batchForm.quantity),
                expirationDate: batchForm.expirationDate
            };

            if (editingId) {
                const response = await api.put(`/inventories/${inventoryId}/products/${productId}/batches/${editingId}`, dadosLote);
                setBatches(prev => prev.map(batch => batch.id === editingId ? response.data : batch));
                setEditingId(null);
            } else {
                const response = await api.post(`/inventories/${inventoryId}/products/${productId}/batches`, dadosLote);
                setBatches(prev => [...prev, response.data]);
            }

            setBatchForm({ supplierBatchCode: "", quantity: "", expirationDate: "" });

            Swal.fire({
                title: "Sucesso!",
                text: editingId ? "Lote atualizado com sucesso." : "Lote adicionado corretamente.",
                icon: "success",
                timer: 2000,
                showConfirmButton: false
            });

        } catch (error) {
            console.error("Erro ao salvar lote:", error);
            Swal.fire({
                title: "Erro!",
                text: "Não foi possível salvar o lote. Verifique as regras.",
                icon: "error",
                confirmButtonColor: "var(--accent, #3085d6)"
            });
        }
    }

    async function deletarLote(batchId) {
        const resultado = await Swal.fire({
            title: "Tem certeza?",
            text: "Esta ação não poderá ser desfeita!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sim, excluir!",
            cancelButtonText: "Cancelar"
        });

        if (!resultado.isConfirmed) return;

        try {
            await api.delete(`/inventories/${inventoryId}/products/${productId}/batches/${batchId}`);
            setBatches(prev => prev.filter(batch => batch.id !== batchId));

            if (editingId === batchId) cancelarEdicao();

            Swal.fire({
                title: "Excluído!",
                text: "O lote foi removido do sistema.",
                icon: "success",
                timer: 1500,
                showConfirmButton: false
            });

        } catch (error) {
            console.error("Erro ao deletar lote:", error);
            Swal.fire({
                title: "Erro!",
                text: "Ocorreu um problem ao tentar excluir o lote.",
                icon: "error",
                confirmButtonColor: "#3085d6"
            });
        }
    }

    function renderStatusBadge(status) {
        const styles = {
            EXPIRED: { bg: "rgba(217, 83, 79, 0.1)", text: "#d9534f", label: "Vencido" },
            WARNING: { bg: "rgba(240, 173, 78, 0.1)", text: "#f0ad4e", label: "Atenção Crítica" },
            MONTH_WARNING: { bg: "rgba(91, 192, 222, 0.1)", text: "#5bc0de", label: "Vence em 30 dias" },
            OK: { bg: "var(--accent-ghost)", text: "var(--accent)", label: "OK" }
        };

        const current = styles[status] || { bg: "#f0f0f0", text: "#333", label: status };

        return (
            <span style={{
                padding: "4px 8px",
                borderRadius: "6px",
                fontSize: "0.8rem",
                fontWeight: "600",
                background: current.bg,
                color: current.text
            }}>
                {current.label}
            </span>
        );
    }

    return (
        <div className="modal-backdrop" style={{ position: "fixed", top: 0, left: 0, width: "100%", height: "100%", background: "rgba(0,0,0,0.5)", display: "flex", justifyContent: "center", alignItems: "center", zIndex: 1000 }}>
            <div className="modal-content" style={{ background: "var(--surface)", padding: "32px", borderRadius: "24px", width: "100%", maxWidth: "600px", maxHeight: "90vh", overflowY: "auto" }}>

                {/* TOPO DO MODAL */}
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "24px" }}>
                    <h2 style={{ margin: 0 }}>Lotes de: <span style={{ color: "var(--accent)" }}>{productName}</span></h2>
                    <button onClick={onClose} style={{ background: "transparent", border: "none", fontSize: "1.5rem", cursor: "pointer", color: "var(--text-3)" }}>✕</button>
                </div>

                {/* FORMULÁRIO DE CADASTRO / EDIÇÃO */}
                <form onSubmit={salvarLote} style={{ display: "flex", gap: "12px", marginBottom: "32px", flexWrap: "wrap" }}>

                    <input
                        style={{ flex: "2 1 200px" }}
                        className="input"
                        required
                        placeholder="Número do Lote (Fornecedor)"
                        value={batchForm.supplierBatchCode}
                        onChange={e => setBatchForm({...batchForm, supplierBatchCode: e.target.value})}
                    />

                    <input style={{ flex: "1 1 80px" }} className="input" type="number" required min="1" placeholder="Qtd" value={batchForm.quantity} onChange={e => setBatchForm({...batchForm, quantity: e.target.value})} />

                    <input
                        style={{ flex: "1 1 160px" }}
                        className="input"
                        type="date"
                        required
                        min={hoje}
                        value={batchForm.expirationDate}
                        onChange={e => setBatchForm({...batchForm, expirationDate: e.target.value})}
                    />

                    <div style={{ width: "100%", display: "flex", gap: "8px", marginTop: "4px" }}>
                        <button className="btn-primary" type="submit" style={{ flex: 2 }}>
                            {editingId ? "Salvar Alterações" : "Adicionar Lote"}
                        </button>
                        {editingId && (
                            <button type="button" onClick={cancelarEdicao} style={{ flex: 1, background: "rgba(0,0,0,0.05)", color: "var(--text-2)", border: "none", borderRadius: "8px", cursor: "pointer", fontWeight: "600" }}>
                                Cancelar
                            </button>
                        )}
                    </div>
                </form>

                {/* LISTAGEM DOS LOTES */}
                <h3>Lotes Atuais</h3>
                <div style={{ display: "flex", flexDirection: "column", gap: "12px" }}>
                    {batches.length === 0 ? (
                        <p style={{ color: "var(--text-3)", textAlign: "center", padding: "16px" }}>Nenhum lote cadastrado para este produto.</p>
                    ) : (
                        batches.map(batch => (
                            <div key={batch.id} style={{ background: "var(--bg)", padding: "16px", borderRadius: "12px", border: "1px solid var(--border)", display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                <div>
                                    <strong style={{ color: "var(--text-1)" }}>
                                        {batch.supplierBatchCode || batch.code || batch.batchCode || `Lote #${batch.id}`}
                                    </strong>

                                    <div style={{ fontSize: "0.9rem", color: "var(--text-2)", marginTop: "4px" }}>
                                        Quantidade: {batch.quantity} | Validade: {new Date(batch.expirationDate).toLocaleDateString("pt-BR")}
                                    </div>
                                    <small style={{ color: "var(--text-3)" }}>
                                        {batch.daysToExpire < 0 ? "Vencido há " : "Faltam "} {Math.abs(batch.daysToExpire)} dias
                                    </small>
                                </div>

                                {/* BOTÕES DE AÇÃO: STATUS + EDITAR + EXCLUIR */}
                                <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
                                    {renderStatusBadge(batch.status)}

                                    <button
                                        onClick={() => prepararEdicao(batch)}
                                        title="Editar Lote"
                                        style={{
                                            background: "rgba(84, 124, 162, 0.1)",
                                            color: "var(--accent, #347ca2)",
                                            border: "none",
                                            borderRadius: "8px",
                                            padding: "6px 10px",
                                            cursor: "pointer",
                                            fontWeight: "bold",
                                            fontSize: "0.9rem"
                                        }}
                                    >
                                        ✏️
                                    </button>

                                    <button
                                        onClick={() => deletarLote(batch.id)}
                                        title="Excluir Lote"
                                        style={{
                                            background: "rgba(217, 83, 79, 0.1)",
                                            color: "#d9534f",
                                            border: "none",
                                            borderRadius: "8px",
                                            padding: "6px 10px",
                                            cursor: "pointer",
                                            fontWeight: "bold",
                                            fontSize: "0.9rem",
                                            transition: "background 0.2s"
                                        }}
                                        onMouseEnter={(e) => e.target.style.background = "rgba(217, 83, 79, 0.2)"}
                                        onMouseLeave={(e) => e.target.style.background = "rgba(217, 83, 79, 0.1)"}
                                    >
                                        🗑️
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                </div>

            </div>
        </div>
    );
}