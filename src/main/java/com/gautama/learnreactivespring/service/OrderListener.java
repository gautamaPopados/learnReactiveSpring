package com.gautama.learnreactivespring.service;

import com.gautama.learnreactivespring.postgres.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class OrderListener {
    private final List<Order> orders = new ArrayList<>();

    @JmsListener(destination = "${queue.orders}")
    public void handleOrderEvent(Order order) {
        log.info("Заказ из очереди: {}", order);

        orders.add(order);

        double avgQuantity = orders.stream()
                .mapToInt(Order::getQuantity)
                .average()
                .orElse(0.0);

        log.info("Среднее количество товаров в заказе: {}", String.format("%.2f", avgQuantity));
    }
}
