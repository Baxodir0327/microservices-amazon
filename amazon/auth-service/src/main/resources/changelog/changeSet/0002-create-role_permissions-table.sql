-- changeset create-role_permissions:b29
CREATE TABLE role_permissions
(
    role_id    SERIAL primary key,
    permission varchar NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role (id)
);