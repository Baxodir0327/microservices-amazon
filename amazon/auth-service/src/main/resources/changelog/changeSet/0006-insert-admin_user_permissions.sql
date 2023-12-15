-- changeset insert-default-role:b29
INSERT INTO role_permissions(role_id,permission)
VALUES (1,'ADD_CATEGORY'),
       (1,'CANCEL_ORDER'),
       (1,'LIST_ORDER'),
       (1,'EDIT_CATEGORY'),
       (1,'DELETE_CATEGORY'),
       (2,'CANCEL_MY_ORDER');
