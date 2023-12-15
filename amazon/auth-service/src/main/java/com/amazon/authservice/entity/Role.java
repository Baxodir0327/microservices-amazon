package com.amazon.authservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("role")
@Data
public class Role {

    @Id
    private Integer id;

    private String name;
}
