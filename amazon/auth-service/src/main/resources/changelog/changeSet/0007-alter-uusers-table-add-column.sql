-- changeset alter-user-table-first:b29

ALTER TABLE users ADD COLUMN account_non_locked bool;

-- changeset alter-user-table-second:b29
ALTER TABLE users ADD COLUMN credentials_non_expired bool;

-- changeset alter-user-table-thirs:b29
ALTER TABLE users ADD COLUMN account_non_expired bool;

-- changeset alter-user-table-fourth:b29
ALTER TABLE users ADD COLUMN enabled bool;