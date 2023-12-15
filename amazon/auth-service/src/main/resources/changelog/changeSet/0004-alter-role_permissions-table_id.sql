-- changeset alter-role_permissions-table_id:b29
ALTER TABLE role_permissions ADD COLUMN id uuid DEFAULT gen_random_uuid();