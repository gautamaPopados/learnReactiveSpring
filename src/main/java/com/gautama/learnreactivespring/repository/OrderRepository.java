package com.gautama.learnreactivespring.repository;

import com.gautama.learnreactivespring.model.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, String> {

    @Query("SELECT COUNT(o) FROM orders o")
    Mono<Long> getTotalOrders();
}
