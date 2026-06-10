import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "./services/api.js";

import IconAlert from "../assets/images/icons/warning.png";
import IconBox from "../assets/images/icons/package.png";

export default function Inventory() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);

    const [expiredBatches, setExpiredBatches] = useState([]);
    const [warningBatches, setWarningBatches] = useState([]);
    const [metrics, setMetrics] = useState({ totalItems: 0, totalWeight: 0 });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        } else {
            navigate("/login");
        }

        carregarDadosDashboard();
    }, [navigate]);

    async function carregarDadosDashboard() {
        try {
            setLoading(true);

            const resExpired = await api.get("/alerts/expired");
            const resMonth = await api.get("/alerts/month");

            setExpiredBatches(resExpired.data || []);
            setWarningBatches(resMonth.data || []);

            let itensSomados = 0;
            let pesoSomado = 0;

            [...(resExpired.data || []), ...(resMonth.data || [])].forEach(batch => {
                itensSomados += batch.quantity || 0;
                pesoSomado += batch.totalWeight || (batch.quantity * (batch.productWeight || 0));
            });

            setMetrics({
                totalItems: itensSomados,
                totalWeight: pesoSomado
            });

        } catch (error) {
            console.error("Erro ao carregar dados reais do Dashboard:", error);
        } finally {
            setLoading(false);
        }
    }

    function handleLogout() {
        localStorage.removeItem("user");
        navigate("/login");
    }

    return (
        <div style={{ backgroundColor: "var(--bg)", minHeight: "100vh", fontFamily: "var(--font-sans)", color: "var(--text-1)" }}>

            {/* HEADER DESIGN COMPACTO */}
            <nav className="nav" style={{ position: "static", background: "var(--surface)", borderBottom: "1px solid var(--border)", boxShadow: "none", height: "70px" }}>
                <div className="nav-inner" style={{ padding: "0 24px", display: "flex", justifyContent: "space-between", alignItems: "center", width: "100%", height: "100%" }}>
                    <a href="/inventory" className="nav-logo" style={{ margin: 0, textDecoration: "none" }}>No<span>Waste</span></a>

                    <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
                        <span style={{ fontSize: "0.9rem", color: "var(--text-2)", fontWeight: "500" }}>
                            Olá, {user?.name ? user.name : "Operador"}
                        </span>
                        <div style={{ width: "36px", height: "36px", borderRadius: "50%", background: "var(--accent)", color: "#fff", display: "flex", alignItems: "center", justifyContent: "center", fontSize: "0.9rem", fontWeight: "700" }}>
                            {user?.name ? user.name.charAt(0).toUpperCase() : "O"}
                        </div>
                        <button onClick={handleLogout} className="btn-ghost" style={{ padding: "6px 12px", fontSize: "0.85rem", border: "1px solid #d9534f", color: "#d9534f", borderRadius: "8px", background: "transparent", cursor: "pointer" }}>
                            Sair
                        </button>
                    </div>
                </div>
            </nav>

            {/* CONTEÚDO CENTRAL */}
            <div className="container" style={{ padding: "40px 24px", maxWidth: "1200px", margin: "0 auto" }}>

                {/* FILA DE INTRODUÇÃO */}
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "32px", flexWrap: "wrap", gap: "16px" }}>
                    <div>
                        <h1 style={{ margin: 0, fontSize: "2rem", fontWeight: "700" }}>Painel de Controle</h1>
                        <p style={{ margin: "4px 0 0 0", color: "var(--text-3)", fontSize: "0.95rem" }}>Visão analítica de vencimentos e lotes sob monitoramento.</p>
                    </div>
                    <button
                        className="btn-primary"
                        onClick={() => navigate("/inventory/list")}
                        style={{ borderRadius: "100px", padding: "12px 24px", fontWeight: "600", fontSize: "0.95rem" }}
                    >
                        Gerenciar Inventários →
                    </button>
                </div>

                {/* DASHBOARD GRID: MÉTRICAS RÁPIDAS COM ICONES PNG */}
                <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(240px, 1fr))", gap: "24px", marginBottom: "40px" }}>

                    {/* CARD 1: ITENS CRÍTICOS */}
                    <div style={{ background: "var(--surface)", padding: "24px", borderRadius: "20px", boxShadow: "var(--shadow-sm)", border: "1px solid var(--border)", display: "flex", alignItems: "center", gap: "20px" }}>
                        <div style={{ background: "rgba(217, 83, 79, 0.08)", padding: "12px", borderRadius: "14px", display: "flex", alignItems: "center", justifyContent: "center" }}>
                            <img src={IconAlert} alt="Alerta" width="28" height="28" />
                        </div>
                        <div>
                            <div style={{ color: "var(--text-3)", fontSize: "0.85rem", fontWeight: "600", textTransform: "uppercase", letterSpacing: "0.5px" }}>Itens Críticos</div>
                            <div style={{ color: "var(--text-1)", fontSize: "1.8rem", fontWeight: "700", marginTop: "4px" }}>
                                {metrics.totalItems} <span style={{ fontSize: "0.9rem", color: "var(--text-3)", fontWeight: "500" }}>unidades</span>
                            </div>
                        </div>
                    </div>

                    {/* CARD 2: VOLUME MONITORADO */}
                    <div style={{ background: "var(--surface)", padding: "24px", borderRadius: "20px", boxShadow: "var(--shadow-sm)", border: "1px solid var(--border)", display: "flex", alignItems: "center", gap: "20px" }}>
                        <div style={{ background: "rgba(84, 124, 162, 0.08)", padding: "12px", borderRadius: "14px", display: "flex", alignItems: "center", justifyContent: "center" }}>
                            <img src={IconBox} alt="Estoque" width="28" height="28" />
                        </div>
                        <div>
                            <div style={{ color: "var(--text-3)", fontSize: "0.85rem", fontWeight: "600", textTransform: "uppercase", letterSpacing: "0.5px" }}>Volume Monitorado</div>
                            <div style={{ color: "var(--accent)", fontSize: "1.8rem", fontWeight: "700", marginTop: "4px" }}>
                                {metrics.totalWeight >= 1000
                                    ? `${(metrics.totalWeight / 1000).toFixed(2)} kg`
                                    : `${metrics.totalWeight} g`
                                }
                            </div>
                        </div>
                    </div>
                </div>

                {/* TABELAS ANALÍTICAS */}
                <div style={{ display: "flex", flexDirection: "column", gap: "32px" }}>

                    {/* TABELA 1: VENCIDOS */}
                    <div style={{ background: "var(--surface)", padding: "28px", borderRadius: "20px", boxShadow: "var(--shadow-sm)", border: "1px solid rgba(217, 83, 79, 0.15)" }}>
                        <div style={{ display: "flex", alignItems: "center", gap: "10px", marginBottom: "20px" }}>
                            <span style={{ width: "10px", height: "10px", borderRadius: "50%", backgroundColor: "#d9534f" }}></span>
                            <h2 style={{ margin: 0, color: "#d9534f", fontSize: "1.25rem", fontWeight: "700" }}>Produtos Vencidos</h2>
                        </div>

                        {expiredBatches.length === 0 ? (
                            <div style={{ background: "rgba(92, 184, 92, 0.06)", padding: "20px", borderRadius: "12px", textAlign: "center", color: "#5cb85c", fontWeight: "600", fontSize: "0.95rem" }}>
                                ✓ Nenhum lote vencido detectado no sistema.
                            </div>
                        ) : (
                            <div style={{ overflowX: "auto" }}>
                                <table style={{ width: "100%", borderCollapse: "collapse", textAlign: "left" }}>
                                    <thead>
                                    <tr style={{ borderBottom: "2px solid var(--border)", color: "var(--text-3)", fontSize: "0.85rem", fontWeight: "600" }}>
                                        <th style={{ padding: "12px 16px" }}>Produto / Descrição</th>
                                        <th style={{ padding: "12px 16px" }}>Cód. Lote</th>
                                        <th style={{ padding: "12px 16px" }}>Qtd</th>
                                        <th style={{ padding: "12px 16px" }}>Data de Validade</th>
                                        <th style={{ padding: "12px 16px" }}>Status</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {expiredBatches.map(batch => (
                                        <tr key={batch.batchId} style={{ borderBottom: "1px solid var(--border)", fontSize: "0.95rem" }}>
                                            <td style={{ padding: "16px", fontWeight: "600" }}>{batch.productName}</td>
                                            <td style={{ padding: "16px" }}>
                                                <code style={{ background: "var(--bg)", padding: "4px 8px", borderRadius: "6px", fontSize: "0.85rem", border: "1px solid var(--border)" }}>
                                                    {batch.batchCode || `Lote #${batch.batchId}`}
                                                </code>
                                            </td>
                                            <td style={{ padding: "16px", fontWeight: "500" }}>{batch.quantity}</td>
                                            <td style={{ padding: "16px" }}>
                                                {batch.expirationDate ? new Date(batch.expirationDate + "T00:00:00").toLocaleDateString("pt-BR") : "-"}
                                            </td>
                                            <td style={{ padding: "16px", color: "#d9534f", fontWeight: "600" }}>
                                                Vencido há {Math.abs(batch.daysToExpire)} dias
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>

                    {/* TABELA 2: EM ALERTA NO MÊS */}
                    <div style={{ background: "var(--surface)", padding: "28px", borderRadius: "20px", boxShadow: "var(--shadow-sm)", border: "1px solid rgba(240, 173, 78, 0.15)" }}>
                        <div style={{ display: "flex", alignItems: "center", gap: "10px", marginBottom: "20px" }}>
                            <span style={{ width: "10px", height: "10px", borderRadius: "50%", backgroundColor: "#f0ad4e" }}></span>
                            <h2 style={{ margin: 0, color: "#f0ad4e", fontSize: "1.25rem", fontWeight: "700" }}>Alertas de Validade (Mês Atual)</h2>
                        </div>

                        {warningBatches.length === 0 ? (
                            <div style={{ background: "var(--bg)", padding: "20px", borderRadius: "12px", textAlign: "center", color: "var(--text-3)", fontSize: "0.95rem" }}>
                                Nenhum produto com vencimento próximo para este mês.
                            </div>
                        ) : (
                            <div style={{ overflowX: "auto" }}>
                                <table style={{ width: "100%", borderCollapse: "collapse", textAlign: "left" }}>
                                    <thead>
                                    <tr style={{ borderBottom: "2px solid var(--border)", color: "var(--text-3)", fontSize: "0.85rem", fontWeight: "600" }}>
                                        <th style={{ padding: "12px 16px" }}>Produto</th>
                                        <th style={{ padding: "12px 16px" }}>Cód. Lote</th>
                                        <th style={{ padding: "12px 16px" }}>Qtd</th>
                                        <th style={{ padding: "12px 16px" }}>Data de Validade</th>
                                        <th style={{ padding: "12px 16px" }}>Tempo Restante</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {warningBatches.map(batch => {
                                        const critical = batch.daysToExpire <= 7;
                                        return (
                                            <tr key={batch.batchId} style={{ borderBottom: "1px solid var(--border)", fontSize: "0.95rem" }}>
                                                <td style={{ padding: "16px", fontWeight: "600" }}>{batch.productName}</td>
                                                <td style={{ padding: "16px" }}>
                                                    <code style={{ background: "var(--bg)", padding: "4px 8px", borderRadius: "6px", fontSize: "0.85rem", border: "1px solid var(--border)" }}>
                                                        {batch.batchCode || `Lote #${batch.batchId}`}
                                                    </code>
                                                </td>
                                                <td style={{ padding: "16px", fontWeight: "500" }}>{batch.quantity}</td>
                                                <td style={{ padding: "16px" }}>
                                                    {batch.expirationDate ? new Date(batch.expirationDate + "T00:00:00").toLocaleDateString("pt-BR") : "-"}
                                                </td>
                                                <td style={{ padding: "16px", fontWeight: "600", color: critical ? "#e67e22" : "#31708f" }}>
                                                    {batch.daysToExpire >= 0 ? `Faltam ${batch.daysToExpire} dias` : "Vence este mês"}
                                                </td>
                                            </tr>
                                        );
                                    })}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>

                </div>
            </div>
        </div>
    );
}