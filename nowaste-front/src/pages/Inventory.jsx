import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"; // Importa o hook para redirecionar após sair
import "./templatemo-622-clearwave.css";

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
        localStorage.removeItem("user"); // Apaga os dados do usuário
        navigate("/"); // Redireciona de volta para a Landing Page (Home)
    }

    return (
        <div style={{ backgroundColor: "var(--bg)", minHeight: "100vh", fontFamily: "var(--font-sans)" }}>

            {/* ── HEADER DO SISTEMA (LOGADO) ── */}
            <nav className="nav" style={{ position: "static", background: "var(--surface)" }}>
                <div className="nav-inner" style={{ padding: "0 20px", display: "flex", justifyContent: "space-between", alignItems: "center", width: "100%" }}>

                    {/* Canto Esquerdo: Logo oficial */}
                    <a href="/" className="nav-logo" style={{ margin: 0 }}>No<span>Waste</span></a>

                    {/* Canto Direito: Bloco de gerenciamento com espaçamento uniforme de 24px */}
                    <div className="nav-cta" style={{ display: "flex", alignItems: "center", gap: "24px", margin: 0 }}>

                        {/* Item 1: Mensagem de saudação */}
                        <span style={{ fontSize: "0.9rem", color: "var(--text-2)", fontWeight: "500", whiteSpace: "nowrap" }}>
                            Olá, {user ? user.name : "Operador"}
                        </span>

                        {/* Item 2: Avatar circular do usuário */}
                        <div style={{
                            width: "32px",
                            height: "32px",
                            borderRadius: "50%",
                            background: "var(--accent)",
                            color: "#fff",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            fontSize: "0.85rem",
                            fontWeight: "700",
                            flexShrink: 0
                        }}>
                            {user ? user.name.charAt(0).toUpperCase() : "O"}
                        </div>

                        {/* Item 3: Botão Sair integrado na mesma identidade da navbar */}
                        <button
                            onClick={handleLogout}
                            className="btn-ghost"
                            style={{
                                fontSize: "0.9rem",
                                border: "none",
                                background: "transparent",
                                cursor: "pointer",
                                padding: "6px 12px",
                                whiteSpace: "nowrap"
                            }}
                        >
                            Sair
                        </button>
                    </div>
                </div>
            </nav>

            {/* ── ÁREA DE TRABALHO DO DESENVOLVEDOR ── */}
            <div className="container" style={{ padding: "60px 20px", maxWidth: "1200px", margin: "0 auto" }}>

                {/* Cabeçalho da Página */}
                <div style={{ marginBottom: "32px" }}>
                    <h1 style={{ color: "var(--text-1)", fontSize: "2rem", fontWeight: "700", margin: 0 }}>
                        Painel de Inventários
                    </h1>
                    <p style={{ color: "var(--text-3)", fontSize: "1rem", marginTop: "6px", marginBottom: 0 }}>
                        Gerencie as validades e lotes dos seus produtos ativos.
                    </p>
                </div>

                {/* Bloco Placeholder Orientativo */}
                <div
                    className="reveal visible"
                    style={{
                        background: "var(--surface-2)",
                        padding: "60px 40px",
                        borderRadius: "24px",
                        boxShadow: "var(--shadow-sm)",
                        border: "2px dashed var(--accent-border)",
                        textAlign: "center"
                    }}
                >
                    <div style={{
                        display: "inline-flex",
                        alignItems: "center",
                        justifyContent: "center",
                        width: "56px",
                        height: "56px",
                        background: "var(--accent-ghost)",
                        borderRadius: "16px",
                        color: "var(--accent)",
                        fontSize: "1.5rem",
                        marginBottom: "20px"
                    }}>
                        🛠️
                    </div>

                    <h3 style={{ color: "var(--text-1)", fontSize: "1.3rem", fontWeight: "600", margin: "0 0 10px 0" }}>
                        Espaço para o Módulo de Listas
                    </h3>

                    <p style={{ color: "var(--text-2)", fontSize: "0.95rem", maxWidth: "500px", margin: "0 auto 24px auto", lineHeight: "1.5" }}>
                        Este container está pronto e estilizado de acordo com o design system do <strong>NoWaste</strong>.
                        Aqui deve ser acoplada a tabela de insumos, cards de lotes ou o consumo da API da rota <code>/products</code>.
                    </p>

                    <button
                        className="btn-primary"
                        style={{
                            padding: "12px 28px",
                            borderRadius: "100px",
                            fontSize: "0.95rem",
                            fontWeight: "600",
                            border: "none",
                            opacity: 0.6,
                            cursor: "not-allowed"
                        }}
                        disabled
                    >
                        + Novo Produto (Aguardando Dev)
                    </button>
                </div>

            </div>
        </div>
    );
}