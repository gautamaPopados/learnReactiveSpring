package com.gautama.learnreactivespring.service;

import com.gautama.learnreactivespring.postgres.entity.Order;
import com.gautama.learnreactivespring.mysql.repository.CustomerRepository;
import com.gautama.learnreactivespring.postgres.repository.OrderRepository;
import com.gautama.learnreactivespring.postgres.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    Random random = new Random();

    private <T> Mono<T> randomElement(Flux<T> flux) {
        return flux
                .index()
                .collectList()
                .flatMap(list -> {
                    if (list.isEmpty()) {
                        log.warn("Невозможно выбрать случайный элемент");
                        return Mono.empty();
                    }
                    int randomIndex = random.nextInt(list.size());
                    return Mono.just(list.get(randomIndex).getT2());
                });
    }

    public Mono<Order> generate() {
        return Mono.zip(
                        randomElement(customerRepository.findAll())
                                .switchIfEmpty(Mono.error(new IllegalStateException("Нет покупателей"))),
                        randomElement(productRepository.findAll())
                                .switchIfEmpty(Mono.error(new IllegalStateException("Нет товаров")))
                )
                .map(tuple -> new Order(
                        UUID.randomUUID(),
                        tuple.getT1().getId(),
                        tuple.getT2().getId(),
                        random.nextInt(100)
                ))
                .flatMap(orderRepository::save)
                .doOnSuccess(o -> log.info("Заказ успешно сохранён: {}", o))
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<Long> getTotalOrders() {
        return orderRepository.getTotalOrders()
                .doOnError(e -> log.error("Ошибка при получении общего количества заказов", e))
                .onErrorReturn(0L);
    }

    public Flux<Order> getOrders(Optional<String> customerName, Optional<String> productTitle) {
        Flux<Order> orders = orderRepository.findAll();

        if (customerName.isPresent()) {
            orders = orders.filterWhen(order ->
                    customerRepository.findById(order.getCustomerId())
                            .map(c -> c.getName().toLowerCase().contains(customerName.get().toLowerCase()))
            );
        }

        if (productTitle.isPresent()) {
            orders = orders.filterWhen(order ->
                    productRepository.findById(order.getProductId())
                            .map(p -> p.getTitle().toLowerCase().contains(productTitle.get().toLowerCase()))
            );
        }

        return orders
                .doOnError(e -> log.error("Ошибка при поиске заказов", e))
                .onErrorResume(e -> Flux.empty());
    }
}
