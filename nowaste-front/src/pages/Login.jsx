import { useState } from "react";
import { api } from "./services/api.js";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    async function handleLogin(e) {
        e.preventDefault();

        try {
            const res = await api.get("/users");

            const user = res.data.find(
                (u) => u.email === email
            );

            if (user) {
                alert("Login OK (sem JWT ainda)");
                localStorage.setItem("user", JSON.stringify(user));
            } else {
                alert("Usuário não encontrado");
            }
        } catch (err) {
            console.log(err);
        }
    }

    return (
        <div>
            <h2>Login</h2>

            <form onSubmit={handleLogin}>
                <input placeholder="Email" onChange={(e) => setEmail(e.target.value)} />
                <input type="password" placeholder="Senha" onChange={(e) => setPassword(e.target.value)} />

                <button type="submit">Entrar</button>
            </form>
        </div>
    );
}