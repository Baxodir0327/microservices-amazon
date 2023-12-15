package com.amazon.apporderservice.payload;

import com.amazon.apporderservice.entity.Order;
import com.amazon.apporderservice.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {

    private UUID id;

    private LocalDateTime createdAt;

    private UUID userId;

    private List<OrderItemDTO> orderItems;
}
