package com.gautama.learnreactivespring;

import com.gautama.learnreactivespring.postgres.entity.Order;
import com.gautama.learnreactivespring.service.OrderListener;
import com.gautama.learnreactivespring.service.OrderService;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;


@SpringBootTest
class MQTest {

    @Rule
    public EmbeddedActiveMQResource broker = new EmbeddedActiveMQResource();

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderListener orderListener;

    @Test
    public void testGenerateOrderAndListen() {
        Mono<Order> orderMono = orderService.generate();
        Order order = orderMono.block();
        assertThat(order).isNotNull();

        await().atMost(java.time.Duration.ofSeconds(2)).untilAsserted(() -> {
            List<Order> orders = orderListener.getOrders();
            assertThat(orders).isNotEmpty();
            assertThat(orders).contains(order);
        });
    }
}