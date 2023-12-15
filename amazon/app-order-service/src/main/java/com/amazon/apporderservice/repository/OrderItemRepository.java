package com.amazon.apporderservice.repository;

import com.amazon.apporderservice.entity.OrderItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrderItemRepository extends R2dbcRepository<OrderItem, UUID> {

    Flux<OrderItem> findAllByOrderId(UUID orderId);
}
