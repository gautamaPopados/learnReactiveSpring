package com.gautama.learnreactivespring.service;

import com.gautama.learnreactivespring.model.Customer;
import com.gautama.learnreactivespring.model.Order;
import com.gautama.learnreactivespring.model.Product;
import com.gautama.learnreactivespring.repository.CustomerRepository;
import com.gautama.learnreactivespring.repository.OrderRepository;
import com.gautama.learnreactivespring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .doOnError(e -> log.error("Ошибка при генерации заказа", e));
    }

    public Mono<Long> getTotalOrders() {
        return orderRepository.getTotalOrders()
                .doOnError(e -> log.error("Ошибка при получении общего количества заказов", e));
    }
}
