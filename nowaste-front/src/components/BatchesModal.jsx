import { useState, useEffect } from "react";
import { api } from "../pages/services/api.js";

export default function BatchesModal({ inventoryId, productId, productName, onClose }) {
    const [batches, setBatches] = useState([]);
    const [batchForm, setBatchForm] = useState({
        batchCode: "",
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

    async function salvarLote(e) {
        e.preventDefault();
        try {
            const novoLote = {
                batchCode: batchForm.batchCode,
                quantity: Number(batchForm.quantity),
                expirationDate: batchForm.expirationDate
            };

            const response = await api.post(`/inventories/${inventoryId}/products/${productId}/batches`, novoLote);

            setBatches(prev => [...prev, response.data]);
            setBatchForm({ batchCode: "", quantity: "", expirationDate: "" });
            alert("Lote adicionado com sucesso!");
        } catch (error) {
            console.error("Erro ao salvar lote:", error);
            alert("Erro ao salvar lote. Verifique as regras no backend.");
        }
    }

    // Função auxiliar para estilizar as badges de status que vêm do Java
    function renderStatusBadge(status) {
        const styles = {
            EXPIRED: { bg: "#rgba(217, 83, 79, 0.1)", text: "#d9534f", label: "Vencido" },
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

                {/* FORMULÁRIO DE CADASTRO */}
                <form onSubmit={salvarLote} style={{ display: "flex", gap: "12px", marginBottom: "32px", flexWrap: "wrap" }}>
                    <input style={{ flex: "1 1 140px" }} className="input" placeholder="Cód. Lote (ex: L123)" value={batchForm.batchCode} onChange={e => setBatchForm({...batchForm, batchCode: e.target.value})} />
                    <input style={{ flex: "1 1 100px" }} className="input" type="number" required min="1" placeholder="Qtd" value={batchForm.quantity} onChange={e => setBatchForm({...batchForm, quantity: e.target.value})} />
                    <input style={{ flex: "1 1 160px" }} className="input" type="date" value={batchForm.expirationDate} onChange={e => setBatchForm({...batchForm, expirationDate: e.target.value})} />

                    <button className="btn-primary" type="submit" style={{ width: "100%", marginTop: "4px" }}>Adicionar Lote</button>
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
                                    <strong style={{ color: "var(--text-1)" }}>{batch.batchCode || `Lote #${batch.id}`}</strong>
                                    <div style={{ fontSize: "0.9rem", color: "var(--text-2)", marginTop: "4px" }}>
                                        Quantidade: {batch.quantity} | Validade: {new Date(batch.expirationDate).toLocaleDateString("pt-BR")}
                                    </div>
                                    <small style={{ color: "var(--text-3)" }}>
                                        {batch.daysToExpire < 0 ? "Vencido há " : "Faltam "} {Math.abs(batch.daysToExpire)} dias
                                    </small>
                                </div>
                                <div>
                                    {renderStatusBadge(batch.status)}
                                </div>
                            </div>
                        ))
                    )}
                </div>

            </div>
        </div>
    );
}