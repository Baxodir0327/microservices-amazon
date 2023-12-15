package com.amazon.apporderservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Order {

    @Id
    private UUID id;

    @Column(value = "user_id")
    private UUID userId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
