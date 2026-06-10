import { BrowserRouter, Routes, Route } from "react-router-dom";

import Home from "./pages/Home.jsx";
import Register from "./pages/Register.jsx";
import Login from "./pages/Login.jsx"
import Inventory from "./pages/Inventory.jsx"
import InventoryProducts from "./pages/InventoryProducts.jsx";
import InventoryList from "./pages/InventoryList.jsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/inventory" element={<Inventory />} />
                <Route path="/inventory/list" element={<InventoryList />} />
                <Route path="/inventory/:inventoryId" element={<InventoryProducts />}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;