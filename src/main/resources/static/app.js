let authToken = "";

const loginBtn = document.getElementById("loginBtn");
const loadProductsBtn = document.getElementById("loadProductsBtn");
const createOrderBtn = document.getElementById("createOrderBtn");
const loadOrdersBtn = document.getElementById("loadOrdersBtn");

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
        loginResult.textContent = "登录失败，请确认密码（password123）";
        return;
    }

    const data = await response.json();
    authToken = data.token;
    loginResult.textContent = `登录成功，token: ${authToken}`;
});

loadProductsBtn.addEventListener("click", async () => {
    const response = await fetch("/api/products");
    const products = await response.json();
    const productList = document.getElementById("productList");
    productList.innerHTML = "";

    products.forEach(product => {
        const li = document.createElement("li");
        li.textContent = `#${product.id} ${product.name} - ¥${product.price}`;
        productList.appendChild(li);
    });
});

createOrderBtn.addEventListener("click", async () => {
    if (!authToken) {
        alert("请先登录");
        return;
    }

    const idsText = document.getElementById("orderProductIds").value.trim();
    const productIds = idsText.split(",").map(item => Number(item.trim())).filter(Boolean);

    const response = await fetch("/api/orders", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${authToken}`
        },
        body: JSON.stringify({ productIds })
    });

    const orderResult = document.getElementById("orderResult");
    if (!response.ok) {
        orderResult.textContent = "创建订单失败，请检查商品ID";
        return;
    }

    const order = await response.json();
    orderResult.textContent = JSON.stringify(order, null, 2);
});

loadOrdersBtn.addEventListener("click", async () => {
    if (!authToken) {
        alert("请先登录");
        return;
    }

    const response = await fetch("/api/orders", {
        headers: { "Authorization": `Bearer ${authToken}` }
    });
    const orders = await response.json();
    document.getElementById("orderResult").textContent = JSON.stringify(orders, null, 2);
});
