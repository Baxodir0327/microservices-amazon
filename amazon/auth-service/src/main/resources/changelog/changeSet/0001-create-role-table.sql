-- changeset create-role-table:b29
CREATE TABLE role
(
    id   INTEGER primary key,
    name varchar NOT NULL UNIQUE
);