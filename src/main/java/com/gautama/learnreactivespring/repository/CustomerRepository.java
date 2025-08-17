package com.gautama.learnreactivespring.repository;

import com.gautama.learnreactivespring.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CustomerRepository  extends ReactiveCrudRepository<Customer, String> {
    Flux<Customer> findByName(String name);
    Mono<Customer> findById(UUID id);
}
