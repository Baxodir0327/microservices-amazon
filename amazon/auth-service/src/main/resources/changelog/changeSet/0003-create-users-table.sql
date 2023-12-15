-- changeset create-users-table:b29
CREATE TABLE users
(
    id       UUID primary key DEFAULT gen_random_uuid(),
    username varchar NOT NULL,
    password varchar NOT NULL,
    role_id  integer NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role (id)
);