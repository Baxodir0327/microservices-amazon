package com.amazon.apporderservice.controller;

import com.amazon.apporderservice.payload.OrderAddDTO;
import com.amazon.apporderservice.payload.OrderDTO;
import com.amazon.apporderservice.service.OrderService;
import com.amazon.apporderservice.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(OrderController.BASE_PATH)
@RequiredArgsConstructor
public class OrderController {
    public static final String BASE_PATH = AppConstants.BASE_PATH + "/order";

    private final OrderService orderService;

    @GetMapping
    public Flux<OrderDTO> allOrders(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return orderService.list(page, size);
    }

    @GetMapping("/my")
    public Flux<OrderDTO> allMyOrders(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return orderService.myOrders(page, size);
    }

    @GetMapping("/{id}")
    public Mono<OrderDTO> one(@PathVariable UUID id) {
        return orderService.byId(id);
    }


    @PostMapping
    public Mono<OrderDTO> create(@RequestBody List<OrderAddDTO> orderAddDTOS) {
        return orderService.add(orderAddDTOS);
    }

    @GetMapping("/test-pagination")
    public Mono<String> test(@RequestParam(defaultValue = "${app.default.page}") int page,
                             @RequestParam(defaultValue = "${app.default.size}") int size) {
        return Mono.just("Page: "+page+", Size: "+size);
    }
}
