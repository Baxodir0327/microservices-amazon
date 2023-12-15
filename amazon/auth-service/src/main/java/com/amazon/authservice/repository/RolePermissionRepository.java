package com.amazon.authservice.repository;

import com.amazon.authservice.entity.Role;
import com.amazon.authservice.entity.RolePermission;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface RolePermissionRepository extends R2dbcRepository<RolePermission, UUID> {

    Flux<RolePermission> findAllByRoleId(Integer roleId);

}
