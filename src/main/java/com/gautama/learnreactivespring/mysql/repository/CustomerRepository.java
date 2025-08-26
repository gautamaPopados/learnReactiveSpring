package com.gautama.learnreactivespring.mysql.repository;

import com.gautama.learnreactivespring.mysql.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
    Mono<Customer> findById(UUID id);
}
