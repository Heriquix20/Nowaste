import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

export default function InventoryProducts() {
    const navigate = useNavigate();
    const { inventoryId } = useParams();

    const [products, setProducts] = useState([]);

    const [productForm, setProductForm] = useState({
        name: "",
        weight: "",
        category: "",
        brand: ""
    });

    // useEffect(() => {
    //     carregarProdutos();
    // }, []);

    // async function carregarProdutos() {
    //     try {
    //         const response = await fetch(`http://localhost:8080/inventories/${inventoryId}/products`);
    //         const data = await response.json();
    //         setProducts(data);
    //     } catch (error) {
    //         console.error(error);
    //     }
    // }

    useEffect(() => {
        setProducts(produtosMock);
    }, []);

        const produtosMock = [
        {
            id: 1,
            name: "Leite Condensado",
            brand: "moça",
            category: "laticinio",
            weight: 395
        },
        {
            id: 2,
            name: "Arroz",
            brand: "prato fino",
            category: "grao",
            weight: 5000
        },
        {
            id: 3,
            name: "Feijão Preto",
            brand: "Kicaldo",
            category: "grao",
            weight: 1000
        }
    ];

    async function criarProduto(e) {
        e.preventDefault();

        try {
            const response = await fetch(
                `http://localhost:8080/inventories/${inventoryId}/products`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        ...productForm,
                        weight: Number(productForm.weight),
                        weightUnit: "g"
                    })
                }
            );

            if (!response.ok) {
                throw new Error("Erro ao criar produto");
            }

            const newProduct = await response.json();

            setProducts(prev => [...prev, newProduct]);

            setProductForm({
                name: "",
                weight: "",
                category: "",
                brand: ""
            });

        } catch (error) {
            console.error(error);
        }
    }



    return (
        <div style={{ backgroundColor: "var(--bg)", minHeight: "100vh" }}>
            <div className="container" style={{ padding: "60px 20px", maxWidth: "1200px", margin: "0 auto" }}>
                <button className="btn-primary" style={{backgroundColor:"var(--accent)", color:"white"}} onClick={() => navigate("/inventory")}>
                    ← Voltar
                </button>

                <div style={{ marginTop: "24px", marginBottom: "32px" }}>
                    <h1>Produtos do Inventário</h1>
                    <p>Gerencie os produtos cadastrados.</p>
                </div>

                <div style={{ background: "var(--surface)", padding: "32px", borderRadius: "24px", marginBottom: "32px" }}>
                    <h2>Novo Produto</h2>

                    <form onSubmit={criarProduto} style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
                        <input className="input" required placeholder="Nome do Produto" value={productForm.name} onChange={(e) => setProductForm({ ...productForm, name: e.target.value })} />
                        <input className="input" required type="number" placeholder="Peso em gramas" value={productForm.weight} onChange={(e) => setProductForm({ ...productForm, weight: e.target.value })} />
                        <input className="input" required placeholder="Categoria" value={productForm.category} onChange={(e) => setProductForm({ ...productForm, category: e.target.value })} />
                        <input className="input" placeholder="Marca" value={productForm.brand} onChange={(e) => setProductForm({ ...productForm, brand: e.target.value })} />

                        <button className="btn-primary" type="submit">
                            Salvar Produto
                        </button>
                    </form>
                </div>

                <div style={{ background: "var(--surface)", padding: "32px", borderRadius: "24px", boxShadow: "var(--shadow-sm)" }}>
                    <h2 style={{ marginTop: 0, marginBottom: "24px", paddingBottom: "16px", borderBottom: "1px solid #eaeaea", color: "var(--text-1)" }}>
                        Produtos Cadastrados
                    </h2>

                    {products.length === 0 ? (
                        <div style={{ background: "var(--bg)", padding: "48px 24px", borderRadius: "16px", textAlign: "center", color: "var(--text-3)", border: "2px dashed #eaeaea" }}>
                            Nenhum produto cadastrado no momento.
                        </div>
                    ) : (
                        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(260px, 1fr))", gap: "24px" }}>
                            {products.map((product) => (
                                <div key={product.id} style={{ background: "var(--bg)", padding: "24px", borderRadius: "16px", boxShadow: "var(--shadow-sm)", border: "1px solid #f0f0f0", display: "flex", flexDirection: "column", gap: "12px" }}>
                                    <h3 style={{ margin: 0, color: "var(--text-1)", fontSize: "1.25rem", borderBottom: "1px solid #eaeaea", paddingBottom: "12px" }}>
                                        {product.name}
                                    </h3>
                                    
                                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", fontSize: "0.95rem" }}>
                                        <strong style={{ color: "var(--text-2)", fontWeight: "600" }}>Marca:</strong> 
                                        <span style={{ color: "var(--text-1)", fontWeight: "500", textAlign: "right" }}>{product.brand || "-"}</span>
                                    </div>
                                    
                                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", fontSize: "0.95rem" }}>
                                        <strong style={{ color: "var(--text-2)", fontWeight: "600" }}>Categoria:</strong> 
                                        <span style={{ color: "var(--text-1)", fontWeight: "500", textAlign: "right" }}>{product.category || "-"}</span>
                                    </div>
                                    
                                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", fontSize: "0.95rem" }}>
                                        <strong style={{ color: "var(--text-2)", fontWeight: "600" }}>Peso:</strong> 
                                        <span style={{ color: "var(--accent)", fontWeight: "700", textAlign: "right" }}>{product.weight} g</span>
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