package com.amazon.apporderservice.service;

import com.amazon.apporderservice.config.ProductClient;
import com.amazon.apporderservice.entity.Order;
import com.amazon.apporderservice.entity.OrderItem;
import com.amazon.apporderservice.payload.OrderAddDTO;
import com.amazon.apporderservice.payload.OrderDTO;
import com.amazon.apporderservice.payload.OrderItemDTO;
import com.amazon.apporderservice.payload.ProductDTO;
import com.amazon.apporderservice.repository.OrderItemRepository;
import com.amazon.apporderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;
    private final RabbitTemplate rabbitTemplate;

    public Flux<OrderDTO> list(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Flux<Order> orders = orderRepository.findAllBy(pageable);

        return orders.flatMap(this::mapToOrderDTO);
    }

    public Mono<OrderDTO> byId(UUID id) {
        Mono<Order> orderMono = orderRepository.findById(id);
        return orderMono.flatMap(this::mapToOrderDTO);
    }


    public Flux<OrderDTO> myOrders(int page, int size) {
        return null;
    }

    @Transactional
    public Mono<OrderDTO> add(List<OrderAddDTO> orderAddDTOS) {
        Order order = createOrderFromAddDTOs();

        Mono<Order> orderMono = orderRepository.save(order);

        return orderMono
                .flatMap(savedOrder -> {
                    Set<String> productIds = getProductIdsFromOrderDTOList(orderAddDTOS);
                    Flux<ProductDTO> fetchAndProcessExternalData = productClient.getProductsByIds(productIds);

                    return fetchAndProcessExternalData
                            .map(productDTO -> {
                                OrderItem orderItem = createOrderItem(productDTO, savedOrder.getId(), orderAddDTOS);
                                return Tuples.of(productDTO, orderItem);
                            })
                            .flatMap(tuple -> orderItemRepository.save(tuple.getT2()).thenReturn(tuple))
                            .collectList()
                            .map(tuples -> {
                                List<ProductDTO> productDTOS = tuples.stream()
                                        .map(Tuple2::getT1)
                                        .collect(Collectors.toList());
                                List<OrderItem> savedOrderItems = tuples.stream()
                                        .map(Tuple2::getT2)
                                        .collect(Collectors.toList());
                                sendNotificationAboutEmail(savedOrder);
                                return toOrderDTO(savedOrder, savedOrderItems, productDTOS);
                            });
                });
//        List<ProductDTO> productDTOS = new LinkedList<>();

//        return orderMono
//                .flatMap(savedOrder -> {
//                            Set<String> productIdsFromOrderDTOList = getProductIdsFromOrderDTOList(orderAddDTOS);
//
//                            Flux<ProductDTO> productDTOFlux = fetchAndProcessExternalData(productIdsFromOrderDTOList);
//
//                            Flux<OrderItem> orderItemFlux = productDTOFlux
//                                    .map(productDTO -> {
//                                        productDTOS.add(productDTO);
//                                        return createOrderItem(productDTO, savedOrder.getId(), orderAddDTOS);
//                                    })
//                                    .flatMap(orderItemRepository::save);
//                            Mono<List<OrderItem>> collectedList = orderItemFlux.collectList();
//                            return collectedList.map(savedOrderItems ->
//                                    toOrderDTO(savedOrder, savedOrderItems, productDTOS));
//                        }
//                );
    }

    private Mono<OrderDTO> mapToOrderDTO(Order order) {
        return orderItemRepository.findAllByOrderId(order.getId())
                .collectList()
                .flatMap(orderItems -> {
                    Set<String> productIds = orderItems.stream()
                            .map(OrderItem::getProductId)
                            .collect(Collectors.toSet());
                    Flux<ProductDTO> productDTOFlux = productClient.getProductsByIds(productIds);
                    Mono<List<ProductDTO>> listMono = productDTOFlux.collectList();
                    return listMono.map(productDTOS -> toOrderDTO(order, orderItems, productDTOS));
                });
    }


    private Set<String> getProductIdsFromOrderDTOList(List<OrderAddDTO> orderAddDTOS) {
        return orderAddDTOS.stream()
                .map(OrderAddDTO::getProductId)
                .collect(Collectors.toSet());
    }

    private Order createOrderFromAddDTOs() {
        return Order.builder()
                .userId(UUID.randomUUID())//todo user id from user service
                .build();
    }

    private OrderItem createOrderItem(ProductDTO productDTO, UUID orderId, List<OrderAddDTO> orderAddDTOS) {
        OrderAddDTO orderAddDTO = orderAddDTOS.stream()
                .filter(dto -> dto.getProductId().equals(productDTO.getId()))
                .findFirst()
                .orElseThrow();
        return OrderItem.builder()
                .orderId(orderId)
                .quantity(orderAddDTO.getCount())
                .unitPrice(productDTO.getPrice())
                .productId(productDTO.getId())
                .build();
    }

//    private Flux<ProductDTO> fetchAndProcessExternalData(Set<String> productIds) {
//        return webClientForProduct
//                .post()
//                .uri("http://localhost:81/api/v1/product/product/by-ids")
//                .body(BodyInserters.fromValue(productIds))
//                .retrieve()
//                .bodyToFlux(ProductDTO.class);
//    }


    private OrderDTO toOrderDTO(Order order, List<OrderItem> orderItems, List<ProductDTO> productDTOS) {
        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .createdAt(order.getCreatedAt())
                .orderItems(mapOrderItemDTOList(orderItems, productDTOS))
                .build();
    }

    private List<OrderItemDTO> mapOrderItemDTOList(List<OrderItem> orderItems, List<ProductDTO> productDTOS) {
        return orderItems.stream().map(orderItem -> OrderItemDTO.builder()

                        .quantity(orderItem.getQuantity())
                        .unitPrice(orderItem.getUnitPrice())
                        .productId(orderItem.getProductId())
                        .productName(productDTOS.stream().filter(productDTO -> productDTO.getId().equals(orderItem.getProductId())).findFirst().orElseThrow().getName())
                        .build())
                .collect(Collectors.toList());
    }


    private void sendNotificationAboutEmail(Order order) {
        order.setCreatedAt(null);
        System.out.println("sending notification service: " + order.getId());
        new Thread(() -> {
            rabbitTemplate.convertAndSend("mainExchangeForB29", "for-only-notification", order.getId().toString());
            rabbitTemplate.convertAndSend("mainExchangeForB29", "for-only-shipping", order);
        }).start();
    }
}
