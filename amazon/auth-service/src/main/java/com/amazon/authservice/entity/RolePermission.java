package com.amazon.authservice.entity;

import com.amazon.authservice.enums.PermissionEnum;
import lombok.Data;
import org.apache.commons.text.translate.UnicodeUnescaper;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("role_permissions")
@Data
public class RolePermission {

    @Id
    private UUID id;

    private Integer roleId;

    private PermissionEnum permission;
}
