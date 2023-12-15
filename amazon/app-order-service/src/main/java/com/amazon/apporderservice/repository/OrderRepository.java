package com.amazon.apporderservice.repository;

import com.amazon.apporderservice.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, UUID> {

    Flux<Order> findAllBy(Pageable pageable);
}
