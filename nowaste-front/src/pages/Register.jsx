import { useState } from "react";
import { api } from "./services/api.js";
import { useNavigate } from "react-router-dom"; // Opcional se for redirecionar depois
import "./templatemo-622-clearwave.css";

export default function Register() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    // Estado para gerenciar o alerta customizado
    const [alertMessage, setAlertMessage] = useState({ text: "", type: "" });

    async function handleSubmit(e) {
        e.preventDefault();
        setAlertMessage({ text: "", type: "" });

        try {
            await api.post("/users", { name, email, password });

            // Alerta de Sucesso
            setAlertMessage({ text: "Empresa cadastrada com sucesso!", type: "success" });
            setName("");
            setEmail("");
            setPassword("");
        } catch (err) {
            console.error(err);
            // Alerta de Erro
            setAlertMessage({ text: "Erro ao realizar o cadastro. Tente novamente.", type: "error" });
        }
    }

    return (
        <div style={{ backgroundColor: "var(--bg)", minHeight: "100vh", fontFamily: "var(--font-sans)" }}>

            {/* NAV BAR CORRIGIDA COM AS REGRAS DE ESPALHAMENTO IGUAL À HOME E AO LOGIN */}
            <nav className="nav" style={{ position: "static", background: "var(--surface)" }}>
                <div className="nav-inner" style={{ padding: "0 20px", display: "flex", justifyContent: "space-between", alignItems: "center", width: "100%" }}>
                    <a href="/" className="nav-logo" style={{ margin: 0 }}>No<span>Waste</span></a>
                    <div className="nav-cta" style={{ margin: 0 }}>
                        <a href="/login" className="btn-ghost" style={{ fontSize: "0.9rem" }}>Entrar</a>
                    </div>
                </div>
            </nav>

            <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: "80vh", padding: "40px 20px" }}>
                <div className="reveal visible" style={{ background: "var(--surface-2)", padding: "40px", borderRadius: "24px", boxShadow: "var(--shadow-lg)", width: "100%", maxWidth: "440px", border: "1px solid var(--border)" }}>

                    <div style={{ textAlign: "center", marginBottom: "32px" }}>
                        <div style={{ display: "inline-flex", alignItems: "center", justifyContent: "center", width: "48px", height: "48px", background: "var(--accent-ghost)", borderRadius: "12px", color: "var(--accent)", fontSize: "1.2rem", marginBottom: "16px" }}>✦</div>
                        <h2 style={{ color: "var(--text-1)", fontSize: "1.8rem", fontWeight: "700", margin: 0 }}>Criar Conta</h2>
                        <p style={{ color: "var(--text-3)", fontSize: "0.9rem", marginTop: "8px", marginBottom: 0 }}>Comece a gerenciar seus lotes de forma inteligente</p>
                    </div>

                    {/* Caixa de Alerta Customizada */}
                    {alertMessage.text && (
                        <div style={{
                            padding: "12px 16px",
                            borderRadius: "12px",
                            marginBottom: "24px",
                            fontSize: "0.9rem",
                            fontWeight: "500",
                            textAlign: "center",
                            background: alertMessage.type === "success" ? "var(--accent-ghost)" : "rgba(217, 83, 79, 0.1)",
                            color: alertMessage.type === "success" ? "var(--accent)" : "#d9534f",
                            border: alertMessage.type === "success" ? "1px solid var(--accent-border)" : "1px solid rgba(217, 83, 79, 0.2)"
                        }}>
                            {alertMessage.type === "success" ? "✓ " : "✕ "} {alertMessage.text}
                        </div>
                    )}

                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label" style={{ color: "var(--text-2)", fontWeight: "500", fontSize: "0.9rem" }}>Nome completo</label>
                            <input type="text" className="form-control" placeholder="Digite seu nome" onChange={(e) => setName(e.target.value)} value={name} required style={{ borderRadius: "10px", borderColor: "var(--border)", padding: "10px 14px" }} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label" style={{ color: "var(--text-2)", fontWeight: "500", fontSize: "0.9rem" }}>E-mail corporativo</label>
                            <input type="email" className="form-control" placeholder="seu@email.com" onChange={(e) => setEmail(e.target.value)} value={email} required style={{ borderRadius: "10px", borderColor: "var(--border)", padding: "10px 14px" }} />
                        </div>

                        <div className="mb-4">
                            <label className="form-label" style={{ color: "var(--text-2)", fontWeight: "500", fontSize: "0.9rem" }}>Senha de acesso</label>
                            <input type="password" className="form-control" placeholder="Mínimo 6 caracteres" onChange={(e) => setPassword(e.target.value)} value={password} required style={{ borderRadius: "10px", borderColor: "var(--border)", padding: "10px 14px" }} />
                        </div>

                        <button className="btn-primary" type="submit" style={{ width: "100%", padding: "14px", borderRadius: "100px", fontSize: "1rem", fontWeight: "600", border: "none", cursor: "pointer" }}>
                            Cadastrar Empresa →
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}