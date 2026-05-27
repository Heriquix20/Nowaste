import { useState } from "react";
import { api } from "./services/api.js";

export default function Register() {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();

        try {
            await api.post("/users", {
                name,
                email,
                password,
            });

            alert("Usuário criado com sucesso!");
        } catch (err) {
            console.log(err);
            alert("Erro ao cadastrar");
        }
    }

    return (
        <div className="bg-light min-vh-100">

            {/* HEADER */}
            <nav className="navbar navbar-dark" style={{ backgroundColor: "#198754" }}>
                <div className="container">
                    <span className="navbar-brand fw-bold">
                        🌱 NoWaste
                    </span>
                </div>
            </nav>

            {/* CONTEÚDO */}
            <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: "90vh" }}>
                <div className="card shadow p-4 border-0" style={{ width: "420px" }}>

                    <h3 className="text-center mb-4" style={{ color: "#198754" }}>
                        Cadastro
                    </h3>

                    <form onSubmit={handleSubmit}>

                        <div className="mb-3">
                            <label className="form-label">Nome</label>
                            <input
                                type="text"
                                className="form-control"
                                placeholder="Digite seu nome"
                                onChange={(e) => setName(e.target.value)}
                                value={name}
                            />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input
                                type="email"
                                className="form-control"
                                placeholder="Digite seu email"
                                onChange={(e) => setEmail(e.target.value)}
                                value={email}
                            />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Senha</label>
                            <input
                                type="password"
                                className="form-control"
                                placeholder="Digite sua senha"
                                onChange={(e) => setPassword(e.target.value)}
                                value={password}
                            />
                        </div>

                        <button
                            className="btn w-100 text-white"
                            type="submit"
                            style={{ backgroundColor: "#198754" }}
                        >
                            Cadastrar
                        </button>

                    </form>
                </div>
            </div>
        </div>
    );
}