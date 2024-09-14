package com.curso.reactive.controller;
import com.curso.reactive.domain.*;
import com.curso.reactive.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping("/accounts/{accountId}/balance")
    public Mono<Double> getBalance(@PathVariable String accountId) {
        return bankService.getBalance(accountId);
    }

    @PostMapping("/accounts/transfer")
    public Mono<String> transferMoney(@RequestBody TransferRequest request) {
        return bankService.transferMoney(request);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public Flux<Transaction> getTransactions(@PathVariable String accountId) {
        return bankService.getTransactions(accountId);
    }

    @GetMapping("/accounts/{accountId}")
    public Mono<Account> getAccount(@PathVariable String accountId) {
        return bankService.getAccount(accountId);
    }

    @PostMapping("/accounts/create")
    public Mono<String> createAccount(@RequestBody CreateAccountRequest request) {
        return bankService.createAccount(request);
    }

    @DeleteMapping("/accounts/{accountId}")
    public Mono<String> closeAccount(@PathVariable String accountId) {
        return bankService.closeAccount(accountId);
    }

    @PutMapping("/accounts/update")
    public Mono<String> updateAccount(@RequestBody UpdateAccountRequest request) {
        return bankService.updateAccount(request);
    }

    @GetMapping("/accounts/{accountId}/profile")
    public Mono<CustomerProfile> getCustomerProfile(@PathVariable String accountId) {
        return bankService.getCustomerProfile(accountId);
    }

    @GetMapping("/customers/{customerId}/loans")
    public Flux<Loan> getActiveLoans(@PathVariable String customerId) {
        return bankService.getActiveLoans(customerId);
    }

    @GetMapping("/accounts/{accountId}/interest")
    public Flux<Double> simulateInterest(@PathVariable String accountId) {
        return bankService.simulateInterest(accountId);
    }

    @GetMapping("/loans/{loanId}")
    public Mono<String> getLoanStatus(@PathVariable String loanId) {
        return bankService.getLoanStatus(loanId);
    }

    @PostMapping("/loans/create")
    public Mono<String> createLoad(@RequestBody Loan request) {
        return bankService.createLoad(request);
    }

    @PostMapping("/customer/create")
    public Mono<String> createCustomer(@RequestBody CustomerProfile request) {
        return bankService.createCustomer(request);
    }
}