-- changeset create-order-table:b29
CREATE TABLE orders
(
    id         UUID primary key DEFAULT gen_random_uuid(),
    user_id    uuid      NOT NULL,
    created_at timestamp NOT NULL
);