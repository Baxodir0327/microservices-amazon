package com.amazon.apporderservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "order_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItem {

    @Id
    private UUID id;

    @Column(value = "order_id")
    private UUID orderId;

    @Column(value = "product_id")
    private String productId;

    private Integer quantity;

    private Double unitPrice;
}
