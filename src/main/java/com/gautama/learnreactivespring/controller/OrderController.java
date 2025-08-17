package com.gautama.learnreactivespring.controller;

import com.gautama.learnreactivespring.model.Order;
import com.gautama.learnreactivespring.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Mono<Order> createOrder() {
        return orderService.generate();
    }

    @GetMapping("/total")
    public Mono<String> getTotalOrders() {
        return orderService.getTotalOrders()
                .map(total -> "Количество заказов: " + total);
    }

    @GetMapping
    public Flux<Order> getOrders(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String productTitle) {

        return orderService.getOrders(Optional.ofNullable(customerName), Optional.ofNullable(productTitle));
    }
}
