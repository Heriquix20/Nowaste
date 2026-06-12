import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Swal from 'sweetalert2';
import "./templatemo-622-clearwave.css";
import IconEdit from "../assets/images/icons/edit.svg";
import IconDelete from "../assets/images/icons/delete.svg";
import IconBox from "../assets/images/icons/package.png";

export default function InventoryList() {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();
    const [inventories, setInventories] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [inventoryForm, setInventoryForm] = useState({
        name: "",
        description: "",
        location: ""
    });

    // Função auxiliar centralizada para obter os Headers com o Token JWT
    const getAuthHeaders = (extraHeaders = {}) => {
        const storedUser = localStorage.getItem("user");
        let token = "";

        if (storedUser) {
            const parsedUser = JSON.parse(storedUser);
            token = parsedUser.token || parsedUser.accessToken || "";
        }

        return {
            "Authorization": `Bearer ${token}`,
            ...extraHeaders
        };
    };

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
        carregarInventarios();
    }, []);

    function handleLogout() {
        localStorage.removeItem("user");
        navigate("/");
    }

    async function carregarInventarios() {
        try {
            const response = await fetch("http://localhost:8080/inventories", {
                headers: getAuthHeaders()
            });

            if (!response.ok) throw new Error("Erro ao buscar inventários");

            const data = await response.json();
            setInventories(data);
        } catch (error) {
            console.error("Erro ao carregar inventários do Java:", error);
            const mocks = [
                { id: 1, name: "Estoque Cozinha", description: "Insumos gerais e perecíveis de uso diário", location: "Cozinha Central", createdAt: "2026-06-09T14:30:00" },
                { id: 2, name: "Dispensa Secos", description: "Grãos, sacarias e enlatados de longa validade", location: "Almoxarifado A", createdAt: "2026-06-08T10:15:00" }
            ];
            setInventories(mocks);
        }
    }

    function prepararEdicao(inventory) {
        setInventoryForm({
            name: inventory.name,
            description: inventory.description,
            location: inventory.location
        });
        setEditingId(inventory.id);
        window.scrollTo({ top: 0, behavior: "smooth" });
    }

    function cancelarEdicao() {
        setInventoryForm({ name: "", description: "", location: "" });
        setEditingId(null);
    }

    async function deletarInventario(id) {
        const resultado = await Swal.fire({
            title: "Tem certeza?",
            text: "Todos os produtos deste estoque e seus lotes serão excluídos!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sim, excluir tudo!",
            cancelButtonText: "Cancelar"
        });

        if (!resultado.isConfirmed) return;

        try {
            const response = await fetch(`http://localhost:8080/inventories/${id}`, {
                method: "DELETE",
                headers: getAuthHeaders()
            });

            if (!response.ok) throw new Error("Erro ao deletar inventário");

            setInventories(prev => prev.filter(inv => inv.id !== id));
            if (editingId === id) cancelarEdicao();

            Swal.fire({
                title: "Deletado!",
                text: "O estoque foi removido com sucesso.",
                icon: "success",
                timer: 1500,
                showConfirmButton: false
            });

        } catch (error) {
            console.error(error);
            Swal.fire({
                title: "Erro!",
                text: "Não foi possível excluir o inventário.",
                icon: "error",
                confirmButtonColor: "#3085d6"
            });
        }
    }

    async function salvarInventario(e) {
        e.preventDefault();
        try {
            const url = editingId
                ? `http://localhost:8080/inventories/${editingId}`
                : "http://localhost:8080/inventories";
            const method = editingId ? "PUT" : "POST";

            const response = await fetch(url, {
                method: method,
                headers: getAuthHeaders({ "Content-Type": "application/json" }),
                body: JSON.stringify(inventoryForm)
            });

            if (!response.ok) throw new Error("Erro ao salvar inventário");

            const savedInventory = await response.json();

            if (editingId) {
                setInventories(prev => prev.map(inv => inv.id === editingId ? savedInventory : inv));
                setEditingId(null);
            } else {
                setInventories(prev => [...prev, savedInventory]);
            }

            setInventoryForm({ name: "", description: "", location: "" });

            Swal.fire({
                title: "Salvo com sucesso!",
                text: editingId ? "As alterações foram gravadas." : "Novo estoque disponível.",
                icon: "success",
                timer: 2000,
                showConfirmButton: false
            });

        } catch (error) {
            console.error(error);
            Swal.fire({
                title: "Erro ao salvar",
                text: "Verifique a conexão ou os dados enviados.",
                icon: "error",
                confirmButtonColor: "var(--accent, #3085d6)"
            });
        }
    }

    return (
        <div style={{ backgroundColor: "var(--bg)", minHeight: "100vh", fontFamily: "var(--font-sans)", color: "var(--text-1)" }}>

            {/* HEADER COMPACTO E LIMPO */}
            <nav className="nav" style={{ position: "static", background: "var(--surface)", borderBottom: "1px solid var(--border)", boxShadow: "none", height: "70px" }}>
                <div className="nav-inner" style={{ padding: "0 24px", display: "flex", justifyContent: "space-between", alignItems: "center", width: "100%", height: "100%" }}>
                    <a href="/inventory" className="nav-logo" style={{ margin: 0, textDecoration: "none" }}>No<span>Waste</span></a>

                    <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
                        <span style={{ fontSize: "0.9rem", color: "var(--text-2)", fontWeight: "500" }}>
                            Olá, {user ? user.name : "Operador"}
                        </span>
                        <div style={{ width: "36px", height: "36px", borderRadius: "50%", background: "var(--accent)", color: "#fff", display: "flex", alignItems: "center", justifyContent: "center", fontSize: "0.9rem", fontWeight: "700" }}>
                            {user ? user.name.charAt(0).toUpperCase() : "O"}
                        </div>
                        <button onClick={handleLogout} className="btn-ghost" style={{ padding: "6px 12px", fontSize: "0.85rem", border: "1px solid #d9534f", color: "#d9534f", borderRadius: "8px", background: "transparent", cursor: "pointer" }}>
                            Sair
                        </button>
                    </div>
                </div>
            </nav>

            {/* CONTEÚDO PRINCIPAL */}
            <div className="container" style={{ padding: "40px 24px", maxWidth: "1300px", margin: "0 auto" }}>

                {/* BOTÃO NAVEGAÇÃO SUPERIOR */}
                <button
                    onClick={() => navigate("/inventory")}
                    style={{ background: "transparent", border: "none", color: "var(--accent)", fontWeight: "600", cursor: "pointer", display: "flex", alignItems: "center", gap: "8px", padding: 0, marginBottom: "24px", fontSize: "0.95rem" }}
                >
                    ← Voltar para Painel de Alertas
                </button>

                {/* AREA EM DUAS COLUNAS ASYMMETRIC (FORMULÁRIO LATERAL + GRID) */}
                <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(320px, 1fr))", gap: "32px", alignItems: "start" }}>

                    <div style={{ background: "var(--surface)", padding: "28px", borderRadius: "20px", boxShadow: "var(--shadow-sm)", border: "1px solid var(--border)", position: "sticky", top: "20px" }}>
                        <h2 style={{ marginTop: 0, marginBottom: "8px", fontSize: "1.4rem", fontWeight: "700" }}>
                            {editingId ? "Editar Local" : "Adicionar Estoque"}
                        </h2>
                        <p style={{ color: "var(--text-3)", fontSize: "0.85rem", margin: "0 0 24px 0" }}>
                            Cadastre uma nova partição física ou lógica para monitorar os prazos.
                        </p>

                        <form onSubmit={salvarInventario} style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
                            <div>
                                <label style={{ display: "block", fontSize: "0.85rem", fontWeight: "600", marginBottom: "6px", color: "var(--text-2)" }}>Nome do Inventário *</label>
                                <input className="input" type="text" required placeholder="Ex: Câmara Fria, Prateleira B..." value={inventoryForm.name} onChange={(e) => setInventoryForm({ ...inventoryForm, name: e.target.value })} style={{ width: "100%" }} />
                            </div>

                            <div>
                                <label style={{ display: "block", fontSize: "0.85rem", fontWeight: "600", marginBottom: "6px", color: "var(--text-2)" }}>Localização Física *</label>
                                <input className="input" type="text" required placeholder="Ex: Corredor 2, Setor de Perecíveis..." value={inventoryForm.location} onChange={(e) => setInventoryForm({ ...inventoryForm, location: e.target.value })} style={{ width: "100%" }} />
                            </div>

                            <div>
                                <label style={{ display: "block", fontSize: "0.85rem", fontWeight: "600", marginBottom: "6px", color: "var(--text-2)" }}>Descrição Curta</label>
                                <textarea className="textarea" placeholder="O que é guardado neste space?" rows={3} value={inventoryForm.description} onChange={(e) => setInventoryForm({ ...inventoryForm, description: e.target.value })} style={{ width: "100%", resize: "none" }} />
                            </div>

                            <div style={{ display: "flex", gap: "12px", marginTop: "8px" }}>
                                <button className="btn-primary" type="submit" style={{ flex: 2, padding: "12px", borderRadius: "10px", fontWeight: "600" }}>
                                    {editingId ? "Salvar Alterações" : "Criar Estoque"}
                                </button>
                                {editingId && (
                                    <button type="button" onClick={cancelarEdicao} style={{ flex: 1, padding: "12px", borderRadius: "10px", background: "rgba(0,0,0,0.05)", color: "var(--text-2)", border: "none", cursor: "pointer", fontWeight: "600" }}>
                                        Cancelar
                                    </button>
                                )}
                            </div>
                        </form>
                    </div>

                    <div style={{ flex: "2" }}>
                        <div style={{ marginBottom: "20px" }}>
                            <h2 style={{ margin: 0, fontSize: "1.5rem", fontWeight: "700" }}>Estoques Ativos ({inventories.length})</h2>
                            <p style={{ margin: "4px 0 0 0", color: "var(--text-3)", fontSize: "0.9rem" }}>Selecione um local abaixo para gerenciar e auditar os produtos e validades.</p>
                        </div>

                        {inventories.length === 0 ? (
                            <div style={{ background: "var(--surface)", padding: "60px 24px", borderRadius: "20px", textAlign: "center", color: "var(--text-3)", border: "2px dashed var(--border)" }}>
                                Nenhum armazém ou setor cadastrado localmente.
                            </div>
                        ) : (
                            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))", gap: "20px" }}>
                                {inventories.map((inventory) => (
                                    <div
                                        key={inventory.id}
                                        style={{
                                            background: "var(--surface)",
                                            padding: "24px",
                                            borderRadius: "16px",
                                            boxShadow: "var(--shadow-sm)",
                                            border: "1px solid var(--border)",
                                            display: "flex",
                                            flexDirection: "column",
                                            justify: "space-between",
                                            transition: "transform 0.2s, box-shadow 0.2s"
                                        }}
                                        onMouseEnter={(e) => {
                                            e.currentTarget.style.transform = "translateY(-4px)";
                                            e.currentTarget.style.boxShadow = "var(--shadow-md)";
                                        }}
                                        onMouseLeave={(e) => {
                                            e.currentTarget.style.transform = "translateY(0)";
                                            e.currentTarget.style.boxShadow = "var(--shadow-sm)";
                                        }}
                                    >
                                        <div>
                                            <div style={{ display: "flex", alignItems: "center", marginBottom: "16px" }}>
                                                <div style={{ background: "rgba(84, 124, 162, 0.08)", padding: "10px", borderRadius: "12px", display: "flex", alignItems: "center", justifyContent: "center" }}>
                                                    <img src={IconBox} alt="Estoque" width="24" height="24" />
                                                </div>
                                            </div>

                                            <h3 style={{ margin: "0 0 8px 0", color: "var(--text-1)", fontSize: "1.2rem", fontWeight: "700" }}>
                                                {inventory.name}
                                            </h3>

                                            <div style={{ fontSize: "0.85rem", color: "var(--text-2)", marginBottom: "8px", display: "flex", gap: "4px" }}>
                                                <span style={{ fontWeight: "600", color: "var(--text-3)" }}>Local:</span> {inventory.location}
                                            </div>

                                            {inventory.description && (
                                                <p style={{ margin: 0, color: "var(--text-3)", fontSize: "0.85rem", lineHeight: "1.4", display: "-webkit-box", WebkitLineClamp: "2", WebkitBoxOrient: "vertical", overflow: "hidden" }}>
                                                    {inventory.description}
                                                </p>
                                            )}
                                        </div>

                                        <div style={{ marginTop: "20px", paddingTop: "12px", borderTop: "1px solid var(--border)", display: "flex", flexDirection: "column", gap: "10px" }}>

                                            <button
                                                className="btn-primary"
                                                onClick={() => navigate(`/inventory/${inventory.id}`)}
                                                style={{ width: "100%", padding: "10px", borderRadius: "8px", fontWeight: "600", fontSize: "0.9rem" }}
                                            >
                                                Ver Produtos →
                                            </button>

                                            <div style={{ display: "flex", gap: "8px" }}>
                                                <button
                                                    onClick={() => prepararEdicao(inventory)}
                                                    style={{ flex: 1, padding: "8px", borderRadius: "6px", background: "rgba(84, 124, 162, 0.1)", border: "none", cursor: "pointer", display: "flex", justifyContent: "center", alignItems: "center", gap: "6px", fontSize: "0.85rem", fontWeight: "600", color: "var(--accent)" }}
                                                >
                                                    <img src={IconEdit} alt="editar" width="14" height="14" />
                                                    Editar
                                                </button>
                                                <button
                                                    onClick={() => deletarInventario(inventory.id)}
                                                    style={{ flex: 1, padding: "8px", borderRadius: "6px", background: "rgba(217, 83, 79, 0.1)", border: "none", cursor: "pointer", display: "flex", justifyContent: "center", alignItems: "center", gap: "6px", fontSize: "0.85rem", fontWeight: "600", color: "#d9534f" }}
                                                >
                                                    <img src={IconDelete} alt="excluir" width="14" height="14" />
                                                    Excluir
                                                </button>
                                            </div>

                                            <div style={{ textAlign: "center", marginTop: "4px" }}>
                                                <span style={{ color: "var(--text-3)", fontSize: "0.75rem" }}>
                                                    Criado em: {inventory.createdAt && new Date(inventory.createdAt).toLocaleDateString("pt-BR")}
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                </div>
            </div>
        </div>
    );
}