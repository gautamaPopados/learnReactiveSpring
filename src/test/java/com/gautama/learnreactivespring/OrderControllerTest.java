package com.gautama.learnreactivespring;

import com.gautama.learnreactivespring.controller.OrderController;
import com.gautama.learnreactivespring.model.Order;
import com.gautama.learnreactivespring.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@WebFluxTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrderService orderService;

    @Test
    void testCreateOrder() {
        Order order = new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 5);

        Mockito.when(orderService.generate()).thenReturn(Mono.just(order));

        webTestClient.post()
                .uri("/order")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Order.class)
                .isEqualTo(order);
    }

    @Test
    void testGetTotalOrders() {
        Mockito.when(orderService.getTotalOrders()).thenReturn(Mono.just(3L));

        webTestClient.get()
                .uri("/order/total")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Количество заказов: 3");
    }

}