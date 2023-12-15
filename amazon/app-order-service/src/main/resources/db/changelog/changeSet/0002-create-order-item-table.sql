-- changeset create-order-item-table:b29
CREATE TABLE order_item
(
    id         UUID primary key default  gen_random_uuid(),
    order_id   uuid             NOT NULL,
    product_id varchar          NOT NULL,
    quantity   integer          NOT NULL,
    unit_price DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id)
);