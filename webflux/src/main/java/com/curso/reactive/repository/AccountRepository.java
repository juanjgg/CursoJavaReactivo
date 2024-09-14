package com.curso.reactive.repository;

import com.curso.reactive.domain.Account;
import com.curso.reactive.domain.CreateAccountRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Account> findByAccountId(String accountId);
}
