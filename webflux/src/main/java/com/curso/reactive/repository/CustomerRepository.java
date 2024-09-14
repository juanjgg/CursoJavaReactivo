package com.curso.reactive.repository;

import com.curso.reactive.domain.CustomerProfile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<CustomerProfile, String> {
    Mono<CustomerProfile> findByCustomerId(String customerId);
}
