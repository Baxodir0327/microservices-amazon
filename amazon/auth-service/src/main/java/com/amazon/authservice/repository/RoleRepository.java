package com.amazon.authservice.repository;

import com.amazon.authservice.entity.Role;
import com.amazon.authservice.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface RoleRepository extends R2dbcRepository<Role, Integer> {


}
