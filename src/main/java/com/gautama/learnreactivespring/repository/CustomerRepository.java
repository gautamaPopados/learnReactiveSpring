package com.gautama.learnreactivespring.repository;

import com.gautama.learnreactivespring.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository  extends ReactiveCrudRepository<Customer, String> {

}
