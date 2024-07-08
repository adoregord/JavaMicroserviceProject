DROP TABLE IF EXISTS Order_item;
DROP TABLE IF EXISTS Orders;


CREATE TABLE IF NOT EXISTS Orders
(
    id BIGSERIAL PRIMARY KEY,
    billing_address VARCHAR(255),
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    order_status VARCHAR(255) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    shipping_address VARCHAR(255) NOT NULL,
    total_amount DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS Order_item
(
    id BIGSERIAL PRIMARY KEY,
    price DOUBLE PRECISION,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    order_id BIGINT NOT NULL,
    CONSTRAINT fkOrder
        FOREIGN KEY(order_id)
            REFERENCES Orders(id)
);