package com.gautama.learnreactivespring.controller;

import com.gautama.learnreactivespring.model.Order;
import com.gautama.learnreactivespring.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Mono<Order> createOrder() {
        return orderService.generate();
    }

    @GetMapping
    public Mono<String> getOrders() {
        return orderService.getTotalOrders()
                .map(total -> "Количество заказов: " + total);
    }
}
