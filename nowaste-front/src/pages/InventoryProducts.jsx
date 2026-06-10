import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { api } from "./services/api.js";
import BatchesModal from "../components/BatchesModal.jsx";
import "./templatemo-622-clearwave.css";

export default function InventoryProducts() {
    const navigate = useNavigate();
    const { inventoryId } = useParams();

    const [products, setProducts] = useState([]);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [productForm, setProductForm] = useState({
        name: "",
        weight: "",
        category: "",
        brand: ""
    });

    useEffect(() => {
        carregarProdutos();
    }, [inventoryId]);

    async function carregarProdutos() {
        try {
            const response = await api.get(`/inventories/${inventoryId}/products`);
            setProducts(response.data);
        } catch (error) {
            console.error("Erro ao buscar produtos do banco:", error);
            alert("Não foi possível carregar os produtos deste inventário.");
        }
    }

    async function criarProduto(e) {
        e.preventDefault();
        try {
            const novoProdutoDTO = {
                name: productForm.name,
                weight: Number(productForm.weight),
                category: productForm.category,
                brand: productForm.brand,
                weightUnit: "g"
            };

            const response = await api.post(`/inventories/${inventoryId}/products`, novoProdutoDTO);
            setProducts(prev => [...prev, response.data]);

            setProductForm({ name: "", weight: "", category: "", brand: "" });
            alert("Produto salvo com sucesso!");
        } catch (error) {
            console.error("Erro ao salvar produto no banco:", error);
            alert("Erro ao salvar produto.");
        }
    }

    return (
        <div style={{ backgroundColor: "var(--bg)", minHeight: "100vh", fontFamily: "var(--font-sans)", color: "var(--text-1)" }}>

            {/* AREA CENTRAL DA TELA */}
            <div className="container" style={{ padding: "40px 24px", maxWidth: "1300px", margin: "0 auto" }}>

                {/* BOTÃO NAVEGAÇÃO SUPERIOR */}
                <button
                    onClick={() => navigate("/inventory/list")}
                    style={{ background: "transparent", border: "none", color: "var(--accent)", fontWeight: "600", cursor: "pointer", display: "flex", alignItems: "center", gap: "8px", padding: 0, marginBottom: "24px", fontSize: "0.95rem" }}
                >
                    ← Voltar para Todos os Inventários
                </button>

                {/* AREA EM DUAS COLUNAS ASYMMETRIC */}
                <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(320px, 1fr))", gap: "32px", alignItems: "start" }}>

                    {/* COLUNA ESQUERDA: FORMULÁRIO LATERAL FIXO */}
                    <div style={{ background: "var(--surface)", padding: "28px", borderRadius: "20px", boxShadow: "var(--shadow-sm)", border: "1px solid var(--border)", position: "sticky", top: "20px" }}>
                        <h2 style={{ marginTop: 0, marginBottom: "8px", fontSize: "1.4rem", fontWeight: "700" }}>Novo Produto</h2>
                        <p style={{ color: "var(--text-3)", fontSize: "0.85rem", margin: "0 0 24px 0" }}>
                            Cadastre a especificação geral do item antes de adicionar as quantidades e validades por lote.
                        </p>

                        <form onSubmit={criarProduto} style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
                            <div>
                                <label style={{ display: "block", fontSize: "0.85rem", fontWeight: "600", marginBottom: "6px", color: "var(--text-2)" }}>Nome do Produto *</label>
                                <input className="input" required placeholder="Ex: Arroz Integral, Leite Desnatado..." value={productForm.name} onChange={(e) => setProductForm({ ...productForm, name: e.target.value })} style={{ width: "100%" }} />
                            </div>

                            <div>
                                <label style={{ display: "block", fontSize: "0.85rem", fontWeight: "600", marginBottom: "6px", color: "var(--text-2)" }}>Peso Líquido (em gramas) *</label>
                                <input className="input" required type="number" placeholder="Ex: 500, 1000..." value={productForm.weight} onChange={(e) => setProductForm({ ...productForm, weight: e.target.value })} style={{ width: "100%" }} />
                            </div>

                            <div>
                                <label style={{ display: "block", fontSize: "0.85rem", fontWeight: "600", marginBottom: "6px", color: "var(--text-2)" }}>Categoria *</label>
                                <input className="input" required placeholder="Ex: Mercearia, Laticínios..." value={productForm.category} onChange={(e) => setProductForm({ ...productForm, category: e.target.value })} style={{ width: "100%" }} />
                            </div>

                            <div>
                                <label style={{ display: "block", fontSize: "0.85rem", fontWeight: "600", marginBottom: "6px", color: "var(--text-2)" }}>Marca / Fabricante</label>
                                <input className="input" placeholder="Ex: Tio João, Nestlé..." value={productForm.brand} onChange={(e) => setProductForm({ ...productForm, brand: e.target.value })} style={{ width: "100%" }} />
                            </div>

                            <button className="btn-primary" type="submit" style={{ padding: "12px", borderRadius: "10px", fontWeight: "600", marginTop: "8px" }}>
                                Salvar Produto
                            </button>
                        </form>
                    </div>

                    {/* COLUNA DIREITA: CATALOGO DE PRODUTOS CADASTRADOS */}
                    <div style={{ flex: "2" }}>
                        <div style={{ marginBottom: "24px" }}>
                            <h2 style={{ margin: 0, fontSize: "1.5rem", fontWeight: "700" }}>Itens Registrados ({products.length})</h2>
                            <p style={{ margin: "4px 0 0 0", color: "var(--text-3)", fontSize: "0.9rem" }}>Visualize os insumos e clique para abrir a auditoria de lotes ativos.</p>
                        </div>

                        {products.length === 0 ? (
                            <div style={{ background: "var(--surface)", padding: "60px 24px", borderRadius: "20px", textAlign: "center", color: "var(--text-3)", border: "2px dashed var(--border)" }}>
                                Nenhum produto cadastrado neste setor de estoque.
                            </div>
                        ) : (
                            /* GRID DE ITENS */
                            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(260px, 1fr))", gap: "20px" }}>
                                {products.map((product) => (
                                    <div
                                        key={product.id}
                                        style={{
                                            background: "var(--surface)",
                                            padding: "24px",
                                            borderRadius: "16px",
                                            boxShadow: "var(--shadow-sm)",
                                            border: "1px solid var(--border)",
                                            display: "flex",
                                            flexDirection: "column",
                                            justifyContent: "space-between",
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
                                            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "16px" }}>
                                                <span style={{ fontSize: "0.8rem", fontWeight: "700", textTransform: "uppercase", color: "var(--text-3)", background: "var(--bg)", padding: "4px 10px", borderRadius: "20px", border: "1px solid var(--border)" }}>
                                                    {product.category || "Geral"}
                                                </span>
                                                <span style={{ fontSize: "1.1rem" }}>🏷️</span>
                                            </div>

                                            <h3 style={{ margin: "0 0 16px 0", color: "var(--text-1)", fontSize: "1.25rem", fontWeight: "700", borderBottom: "1px solid var(--border)", paddingBottom: "12px" }}>
                                                {product.name}
                                            </h3>

                                            <div style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
                                                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", fontSize: "0.9rem" }}>
                                                    <span style={{ color: "var(--text-3)", fontWeight: "500" }}>Marca:</span>
                                                    <span style={{ color: "var(--text-1)", fontWeight: "600" }}>{product.brand || "-"}</span>
                                                </div>
                                                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", fontSize: "0.9rem" }}>
                                                    <span style={{ color: "var(--text-3)", fontWeight: "500" }}>Conteúdo:</span>
                                                    <span style={{ color: "var(--accent)", fontWeight: "700" }}>{product.weight} {product.weightUnit || "g"}</span>
                                                </div>
                                            </div>
                                        </div>

                                        <button
                                            className="btn-primary"
                                            onClick={() => setSelectedProduct(product)}
                                            style={{ width: "100%", padding: "12px", borderRadius: "8px", fontWeight: "600", fontSize: "0.9rem", marginTop: "24px" }}
                                        >
                                            Ver Lotes / Validade
                                        </button>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                </div>
            </div>

            {/* CONTROLE INJETADO DO MODAL DE LOTES */}
            {selectedProduct && (
                <BatchesModal
                    inventoryId={inventoryId}
                    productId={selectedProduct.id}
                    productName={selectedProduct.name}
                    onClose={() => setSelectedProduct(null)}
                />
            )}
        </div>
    );
}