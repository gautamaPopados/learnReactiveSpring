package com.gautama.learnreactivespring;

import com.gautama.learnreactivespring.mysql.entity.Customer;
import com.gautama.learnreactivespring.postgres.entity.Order;
import com.gautama.learnreactivespring.postgres.entity.Product;
import com.gautama.learnreactivespring.mysql.repository.CustomerRepository;
import com.gautama.learnreactivespring.postgres.repository.OrderRepository;
import com.gautama.learnreactivespring.postgres.repository.ProductRepository;
import com.gautama.learnreactivespring.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private JmsTemplate jmsTemplate;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(customerRepository, productRepository, orderRepository, jmsTemplate);
    }

    @Test
    void generateOrderSuccess() {
        Customer customer = new Customer(UUID.randomUUID(), "Иван");
        Product product = new Product(UUID.randomUUID(), "Телефон", 1.0);
        Order order = new Order(UUID.randomUUID(), customer.getId(), product.getId(), 5);

        Mockito.when(customerRepository.findAll()).thenReturn(Flux.just(customer));
        Mockito.when(productRepository.findAll()).thenReturn(Flux.just(product));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(Mono.just(order));

        StepVerifier.create(orderService.generate())
                .expectNextMatches(o -> o.getCustomerId().equals(customer.getId()) &&
                        o.getProductId().equals(product.getId()))
                .verifyComplete();
    }

    @Test
    void generateOrderNoCustomers() {
        Mockito.when(customerRepository.findAll()).thenReturn(Flux.empty());
        Mockito.when(productRepository.findAll()).thenReturn(Flux.just(new Product(UUID.randomUUID(), "Телефон", 1.0)));

        StepVerifier.create(orderService.generate())
                .verifyComplete();
    }

    @Test
    void getTotalOrdersSuccess() {
        Mockito.when(orderRepository.getTotalOrders()).thenReturn(Mono.just(10L));

        StepVerifier.create(orderService.getTotalOrders())
                .expectNext(10L)
                .verifyComplete();
    }

    @Test
    void getTotalOrdersWithError() {
        Mockito.when(orderRepository.getTotalOrders()).thenReturn(Mono.error(new RuntimeException("Ошибка БД")));

        StepVerifier.create(orderService.getTotalOrders())
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    void getOrdersWithoutFilters() {
        Order o1 = new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 2);
        Order o2 = new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 3);

        Mockito.when(orderRepository.findAll()).thenReturn(Flux.just(o1, o2));

        StepVerifier.create(orderService.getOrders(Optional.empty(), Optional.empty()))
                .expectNext(o1, o2)
                .verifyComplete();
    }

    @Test
    void getOrdersWithCustomerNameFilter() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "Иван");
        Order order = new Order(UUID.randomUUID(), customerId, UUID.randomUUID(), 1);

        Mockito.when(orderRepository.findAll()).thenReturn(Flux.just(order));
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Mono.just(customer));

        StepVerifier.create(orderService.getOrders(Optional.of("Иван"), Optional.empty()))
                .expectNext(order)
                .verifyComplete();
    }

    @Test
    void getOrdersWithProductTitleFilter() {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Телефон", 1.0);
        Order order = new Order(UUID.randomUUID(), UUID.randomUUID(), productId, 1);

        Mockito.when(orderRepository.findAll()).thenReturn(Flux.just(order));
        Mockito.when(productRepository.findById(productId)).thenReturn(Mono.just(product));

        StepVerifier.create(orderService.getOrders(Optional.empty(), Optional.of("Телефон")))
                .expectNext(order)
                .verifyComplete();
    }

    @Test
    void getOrdersWithError() {
        Mockito.when(orderRepository.findAll()).thenReturn(Flux.error(new RuntimeException("Ошибка БД")));

        StepVerifier.create(orderService.getOrders(Optional.empty(), Optional.empty()))
                .verifyComplete();
    }
}