let authToken = "";

const registerBtn = document.getElementById("registerBtn");
const loginBtn = document.getElementById("loginBtn");
const loadProductsBtn = document.getElementById("loadProductsBtn");
const createOrderBtn = document.getElementById("createOrderBtn");
const loadOrdersBtn = document.getElementById("loadOrdersBtn");

registerBtn.addEventListener("click", async () => {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    const response = await fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    const loginResult = document.getElementById("loginResult");
    if (!response.ok) {
        loginResult.textContent = "Register failed. Username may already exist.";
        return;
    }

    const data = await response.json();
    authToken = data.token;
    loginResult.textContent = `Register successful. Token: ${authToken}`;
});

loginBtn.addEventListener("click", async () => {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    const loginResult = document.getElementById("loginResult");
    if (!response.ok) {
        loginResult.textContent = "Login failed. Please check your credentials.";
        return;
    }

    const data = await response.json();
    authToken = data.token;
    loginResult.textContent = `Login successful. Token: ${authToken}`;
});

loadProductsBtn.addEventListener("click", async () => {
    const response = await fetch("/api/products");
    const products = await response.json();
    const productList = document.getElementById("productList");
    productList.innerHTML = "";

    products.forEach(product => {
        const li = document.createElement("li");
        li.textContent = `#${product.id} ${product.name} - $${product.price}`;
        productList.appendChild(li);
    });
});

function parseOrderItems(inputText) {
    return inputText
        .split(",")
        .map(item => item.trim())
        .filter(Boolean)
        .map(entry => {
            const [productIdText, quantityText] = entry.split(":").map(part => part.trim());
            const productId = Number(productIdText);
            const quantity = Number(quantityText ?? "1");
            return { productId, quantity };
        })
        .filter(item => Number.isFinite(item.productId) && Number.isFinite(item.quantity) && item.quantity > 0);
}

createOrderBtn.addEventListener("click", async () => {
    if (!authToken) {
        alert("Please login first.");
        return;
    }

    const itemsText = document.getElementById("orderItems").value.trim();
    const items = parseOrderItems(itemsText);

    const response = await fetch("/api/orders", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${authToken}`
        },
        body: JSON.stringify({ items })
    });

    const orderResult = document.getElementById("orderResult");
    if (!response.ok) {
        orderResult.textContent = "Order creation failed. Please verify product IDs and quantities.";
        return;
    }

    const order = await response.json();
    orderResult.textContent = JSON.stringify(order, null, 2);
});

loadOrdersBtn.addEventListener("click", async () => {
    if (!authToken) {
        alert("Please login first.");
        return;
    }

    const response = await fetch("/api/orders", {
        headers: { "Authorization": `Bearer ${authToken}` }
    });
    const orders = await response.json();
    document.getElementById("orderResult").textContent = JSON.stringify(orders, null, 2);
});
