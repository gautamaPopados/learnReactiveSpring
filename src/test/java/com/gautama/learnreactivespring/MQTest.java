package com.gautama.learnreactivespring;

import com.gautama.learnreactivespring.postgres.entity.Order;
import com.gautama.learnreactivespring.service.OrderListener;
import com.gautama.learnreactivespring.service.OrderService;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;


@RunWith(SpringRunner.class)
@SpringBootTest(properties = "order.scheduler.enabled=false")
@Testcontainers
public class MQTest {

    @ClassRule
    public static GenericContainer<?> activeMqContainer =
            new GenericContainer<>(DockerImageName.parse("rmohr/activemq:5.14.3"))
                    .withExposedPorts(61616);

    @ClassRule
    public static GenericContainer<?> mysqlContainer =
            new GenericContainer<>(DockerImageName.parse("mysql:8.0"))
                    .withEnv("MYSQL_ROOT_PASSWORD", "1234")
                    .withEnv("MYSQL_DATABASE", "customersdb")
                    .withExposedPorts(3306);

    @BeforeClass
    public static void setupProperties() {
        System.setProperty("spring.activemq.broker-url",
                "tcp://" + activeMqContainer.getHost() + ":" + activeMqContainer.getMappedPort(61616));
        System.setProperty("spring.activemq.user", "admin");
        System.setProperty("spring.activemq.password", "admin");

        System.setProperty("app.r2dbc.mysql.url",
                "r2dbc:mysql://" + mysqlContainer.getHost() + ":" + mysqlContainer.getMappedPort(3306) + "/customersdb");
        System.setProperty("app.r2dbc.mysql.username", "root");
        System.setProperty("app.r2dbc.mysql.password", "1234");
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderListener orderListener;

    @Test
    void testListenerReceivesOrder() {
        Order order = orderService.generate().block();
        Assertions.assertNotNull(order);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Assertions.assertTrue(orderListener.getOrders().contains(order));
        });
    }
}