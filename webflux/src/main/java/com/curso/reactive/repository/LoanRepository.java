package com.curso.reactive.repository;

import com.curso.reactive.domain.Loan;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface LoanRepository extends ReactiveMongoRepository<Loan, String> {
    Flux<Loan> findByCustomerId(String customerId);
    Flux<Loan> findByCustomerIdAndStatus(String customerId, String status);

    Mono<Loan> findByLoanId(String loanId);
}
