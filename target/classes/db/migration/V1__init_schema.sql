CREATE TABLE IF NOT EXISTS app_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price NUMERIC(12,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_users(id),
    total_amount NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id),
    quantity INT NOT NULL,
    unit_price NUMERIC(12,2) NOT NULL
);

INSERT INTO app_users (username, password)
VALUES ('demo', 'password123')
ON CONFLICT (username) DO NOTHING;

INSERT INTO products (id, name, description, price)
VALUES
    (1, 'Huawei Watch', 'Smart watch for fitness and productivity', 1999.00),
    (2, 'Wireless Earbuds', 'Noise cancellation earbuds', 799.00),
    (3, 'MateBook', 'Lightweight performance laptop', 6999.00)
ON CONFLICT (id) DO NOTHING;
