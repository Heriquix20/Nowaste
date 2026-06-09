import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./templatemo-622-clearwave.css";
import IconEdit from "../assets/images/icons/edit.svg"; 
import IconDelete from "../assets/images/icons/delete.svg";

export default function Inventory() {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();

    // Recupera os dados do usuário salvos no localStorage pelo login
    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
    }, []);

    // Função para deslogar do sistema
    function handleLogout() {
        localStorage.removeItem("user");
        navigate("/");
    }

    // Estados para armazenar inventários, formulário e controle de edição
    const [inventories, setInventories] = useState([]);
    const [editingId, setEditingId] = useState(null); 
    const [inventoryForm, setInventoryForm] = useState({
        name: "",
        description: "",
        location: ""
    });

    // Função para carregar os inventários
    useEffect(() => {
        carregarInventarios();
    }, []);

    async function carregarInventarios() {
        const data = [
            { id: 1, name: "aaaaaaa", description: "etc", location: "local", createdAt: "2026-06-09T14:30:00" },
            { id: 2, name: "bbbbbbbbb", description: "etc", location: "local", createdAt: "2026-06-08T10:15:00" },
            { id: 3, name: "cccccccccc", description: "etc", location: "local", createdAt: "2026-06-07T09:00:00" },
            { id: 4, name: "ddddddd", description: "etc", location: "local", createdAt: "2026-06-07T09:00:00" },
        ];
        setInventories(data);
    }

    function prepararEdicao(inventory) {
        setInventoryForm({
            name: inventory.name,
            description: inventory.description,
            location: inventory.location
        });
        setEditingId(inventory.id);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
    function cancelarEdicao() {
        setInventoryForm({ name: "", description: "", location: "" });
        setEditingId(null);
    }

    // Função para excluir o inventário
    async function deletarInventario(id) {
        const confirmacao = window.confirm("Tem certeza que deseja excluir este inventário?");
        if (!confirmacao) return;

        try {
            const response = await fetch(`http://localhost:8080/inventories/${id}`, {
                method: "DELETE",
            });

            if (!response.ok) {
                throw new Error("Erro ao deletar inventário");
            }

            setInventories(prev => prev.filter(inv => inv.id !== id));
            
            if (editingId === id) cancelarEdicao();

        } catch (error) {
            console.error(error);
            alert("Erro ao excluir inventário. Verifique o console.");
        }
    }

    // Função para editar e salvar
    async function salvarInventario(e) {
        e.preventDefault();

        try {
            const url = editingId 
                ? `http://localhost:8080/inventories/${editingId}` 
                : "http://localhost:8080/inventories";
            const method = editingId ? "PUT" : "POST";

            const response = await fetch(url, {
                method: method,
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(inventoryForm)
            });

            if (!response.ok) {
                throw new Error("Erro ao salvar inventário");
            }

            const savedInventory = await response.json();

            if (editingId) {
                setInventories(prev => prev.map(inv => inv.id === editingId ? savedInventory : inv));
                setEditingId(null);
            } else {
                setInventories(prev => [...prev, savedInventory]);
            }

            setInventoryForm({ name: "", description: "", location: "" });

        } catch (error) {
            console.error(error);
            alert("Erro ao salvar inventário");
        }
    }

    return (
        <div style={{ backgroundColor: "var(--bg)", minHeight: "100vh", fontFamily: "var(--font-sans)" }}>
            {/* HEADER */}
            <nav className="nav" style={{ position: "static", background: "var(--surface)" }}>
                <div className="nav-inner" style={{ padding: "0 20px", display: "flex", justifyContent: "space-between", alignItems: "center", width: "100%" }}>
                    <a href="/" className="nav-logo" style={{ margin: 0 }}>
                        No<span>Waste</span>
                    </a>

                    <div className="nav-cta" style={{ display: "flex", alignItems: "center", gap: "24px", margin: 0 }}>
                        <span style={{ fontSize: "0.9rem", color: "var(--text-2)", fontWeight: "500", whiteSpace: "nowrap" }}>
                            Olá, {user ? user.name : "Operador"}
                        </span>

                        <div style={{ width: "32px", height: "32px", borderRadius: "50%", background: "var(--accent)", color: "#fff", display: "flex", alignItems: "center", justifyContent: "center", fontSize: "0.85rem", fontWeight: "700" }}>
                            {user ? user.name.charAt(0).toUpperCase() : "O"}
                        </div>

                        <button onClick={handleLogout} className="btn-ghost" style={{ fontSize: "0.9rem", border: "none", background: "transparent", cursor: "pointer", padding: "6px 12px" }}>
                            Sair
                        </button>
                    </div>
                </div>
            </nav>

            {/* CONTEÚDO */}
            <div className="container" style={{ padding: "60px 20px", maxWidth: "1200px", margin: "0 auto" }}>
                
                {/* TÍTULO */}
                <div style={{ marginBottom: "32px" }}>
                    <h1 style={{ color: "var(--text-1)", fontSize: "2rem", fontWeight: "700", margin: 0 }}>
                        Painel de Inventários
                    </h1>
                    <p style={{ color: "var(--text-3)", fontSize: "1rem", marginTop: "6px", marginBottom: 0 }}>
                        Gerencie seus inventários e produtos.
                    </p>
                </div>

                {/* FORMULÁRIO */}
                <div style={{ background: "var(--surface)", padding: "32px", borderRadius: "24px", boxShadow: "var(--shadow-sm)", marginBottom: "32px"}}>
                    <h2 style={{ marginTop: 0, marginBottom: "24px", color: "var(--text-1)" }}>
                        <img src="nowaste-front\src\assets\images\icons\delete.svg" alt="" />
                        {editingId ? "Editar Inventário" : "Novo Inventário"}
                    </h2>

                    <form onSubmit={salvarInventario} style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
                        <input className="input" type="text" required placeholder="Nome do inventário" value={inventoryForm.name} onChange={(e) => setInventoryForm({ ...inventoryForm, name: e.target.value })} />
                        <input className="input" type="text" required placeholder="Localização" value={inventoryForm.location} onChange={(e) => setInventoryForm({ ...inventoryForm, location: e.target.value })} />
                        <textarea className="textarea" placeholder="Descrição" rows={4} value={inventoryForm.description} onChange={(e) => setInventoryForm({ ...inventoryForm, description: e.target.value })} />

                        <div style={{ display: "flex", gap: "16px" }}>
                            <button className="btn-primary" type="submit" style={{ flex: 1 }}>
                                {editingId ? "Atualizar Inventário" : "Criar Inventário"}
                            </button>
                            
                            {/* Botão de cancelar aparece apenas se estiver no modo de edição */}
                            {editingId && (
                                <button type="button" onClick={cancelarEdicao} style={{ flex: 1, padding: "12px", borderRadius: "8px", background: "#f0f0f0", color: "#333", border: "none", cursor: "pointer", fontWeight: "600" }}>
                                    Cancelar Edição
                                </button>
                            )}
                        </div>
                    </form>
                </div>
                
                {/* LISTA DE INVENTÁRIOS */}
                <div style={{ background: "var(--surface)", padding: "32px", borderRadius: "24px", boxShadow: "var(--shadow-sm)", marginBottom: "32px" }}>
                    <h2 style={{ marginTop: 0, marginBottom: "24px", paddingBottom: "16px", borderBottom: "1px solid #eaeaea", color: "var(--text-1)" }}>
                        Inventários Cadastrados
                    </h2>

                    {inventories.length === 0 ? (
                        <div style={{ background: "var(--bg)", padding: "48px 24px", borderRadius: "16px", textAlign: "center", color: "var(--text-3)", border: "2px dashed #eaeaea" }}>
                            Nenhum inventário cadastrado no momento.
                        </div>
                    ) : (
                        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))", gap: "24px" }}>
                            {inventories.map((inventory) => (
                                <div key={inventory.id} style={{ background: "var(--bg)", padding: "24px", borderRadius: "16px", boxShadow: "var(--shadow-sm)", display: "flex", flexDirection: "column", justifyContent: "space-between", border: "1px solid #f0f0f0" }}>
                                    <div>
                                        <h3 style={{ marginTop: 0, marginBottom: "12px", color: "var(--text-1)", fontSize: "1.25rem" }}>
                                            {inventory.name}
                                        </h3>

                                        <p style={{ margin: "0 0 8px 0", color: "var(--text-2)", fontSize: "0.95rem" }}>
                                            <strong style={{ color: "var(--text-1)" }}>Local:</strong> {inventory.location}
                                        </p>

                                        {inventory.description && (
                                            <p style={{ margin: 0, color: "var(--text-3)", fontSize: "0.9rem", lineHeight: "1.5" }}>
                                                {inventory.description}
                                            </p>
                                        )}
                                    </div>

                                    <div style={{ display: "flex", flexDirection: "column", gap: "12px", marginTop: "24px", paddingTop: "16px", borderTop: "1px solid #eaeaea" }}>
                                        <small style={{ color: "var(--text-3)", fontSize: "0.8rem", textAlign: "center" }}>
                                            {inventory.createdAt && new Date(inventory.createdAt).toLocaleString("pt-BR", { day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit" })}
                                        </small>

                                        <div style={{ display: "flex", gap: "8px" }}>
                                            <button 
                                                onClick={() => prepararEdicao(inventory)} 
                                                style={{ flex: 1, padding: "8px", borderRadius: "6px", background: "#547ca2", color: "var(--text-1)", cursor: "pointer", fontWeight: "600", display: "flex", justifyContent: "center", alignItems: "center" }}
                                            >
                                                <img src={IconEdit} alt="editar" width="20" height="20" />
                                            </button>
                                            
                                            <button 
                                                onClick={() => deletarInventario(inventory.id)} 
                                                style={{ flex: 1, padding: "8px", borderRadius: "6px", background: "#a14646", color: "#dc2626", cursor: "pointer", fontWeight: "600", display: "flex", justifyContent: "center", alignItems: "center" }}
                                            >
                                                <img src={IconDelete} alt="excluir" width="20" height="20" />
                                            </button>
                                        </div>

                                        <button className="btn-primary" onClick={() => navigate(`/inventory/${inventory.id}`)} style={{ width: "100%", padding: "12px", borderRadius: "8px", fontWeight: "600", marginTop: "4px" }}>
                                            Ver Produtos
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}