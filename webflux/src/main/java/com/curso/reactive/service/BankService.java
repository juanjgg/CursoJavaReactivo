package com.curso.reactive.service;

import com.curso.reactive.domain.*;
import com.curso.reactive.exception.BusinessException;
import com.curso.reactive.repository.AccountRepository;
import com.curso.reactive.repository.CustomerRepository;
import com.curso.reactive.repository.LoanRepository;
import com.curso.reactive.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class BankService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private  AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    public Mono<Double> getBalance(String accountId) {
        // Caso de uso: Consultar el saldo actual de una cuenta bancaria. Sino hay balance se debe tener un valor de 0.0
        return accountRepository.findByAccountId(accountId)
                .map(Account::getBalance)
                .defaultIfEmpty(0.0);
    }

    public Mono<String> transferMoney(TransferRequest request) {
        // Caso de uso: Transferir dinero de una cuenta a otra. Hacer llamado de otro flujo simulando el llamado
        return accountRepository.findByAccountId(request.getFromAccount())
                .flatMap(fromAccount -> accountRepository.findByAccountId(request.getToAccount())
                        .flatMap(toAccount -> {
                            fromAccount.setBalance(fromAccount.getBalance() - request.getAmount());
                            toAccount.setBalance(toAccount.getBalance() + request.getAmount());
                            Transaction fromTransaction =
                                    new Transaction(UUID.randomUUID().toString(),request.getFromAccount(),
                                            request.getAmount()*-1);
                            Transaction toTransaction =
                                    new Transaction(UUID.randomUUID().toString(),request.getToAccount(),
                                            request.getAmount());
                            return accountRepository.save(fromAccount)
                                    .then(accountRepository.save(toAccount))
                                    .then(transactionRepository.save(fromTransaction))
                                    .then(transactionRepository.save(toTransaction))
                                    .then(Mono.just("Transferencia exitosa"));
                        }))
                .switchIfEmpty(Mono.error(new BusinessException("Cuenta no encontrada")));
    }

    public Flux<Transaction> getTransactions(String accountId) {
        // Caso de uso: Consultar el historial de transacciones de una cuenta bancaria.
        return transactionRepository.findByAccountId(accountId);
    }

    public Mono<String> createAccount(CreateAccountRequest request) {
        // Caso de uso: Crear una nueva cuenta bancaria con un saldo inicial.
        Account account = new Account(request.getAccountId(), request.getCustomerId(), request.getInitialBalance());
        return accountRepository.save(account)
                .map(savedAccount -> "Cuenta creada con ID: " + savedAccount.getIdAccount());
    }

    public Mono<String> closeAccount(String accountId) {
        // Caso de uso: Cerrar una cuenta bancaria especificada. Verificar que la ceunta exista y si no existe debe retornar un error controlado
        return accountRepository.findByAccountId(accountId)
                .flatMap(account -> accountRepository.delete(account)
                        .then(Mono.just("Cuenta con ID: " + accountId + " ha sido cerrada.")))
                .switchIfEmpty(Mono.error(new BusinessException("Cuenta no encontrada")));
    }

    public Mono<String> updateAccount(UpdateAccountRequest request) {
        // Caso de uso: Actualizar la información de una cuenta bancaria especificada. Verificar que la ceunta exista y si no existe debe retornar un error controlado
        return accountRepository.findByAccountId(request.getAccountId())
                .flatMap(account -> {
                    account.setBalance(request.getNewBalance());
                    account.setCustomerId(request.getNewCustomerId());
                    return accountRepository.save(account)
                            .then(Mono.just("Cuenta con ID: " + request.getAccountId() + " ha sido actualizada."));
                })
                .switchIfEmpty(Mono.error(new BusinessException("Cuenta no encontrada")));
    }

    public Mono<CustomerProfile> getCustomerProfile(String accountId) {
        // Caso de uso: Consultar el perfil del cliente que posee la cuenta bancaria. Obtener los valores por cada uno de los flujos y si no existe alguno debe presentar un error
        return accountRepository.findByAccountId(accountId)
                .switchIfEmpty(Mono.error(new BusinessException("Account not found")))
                .flatMap(account -> customerRepository.findByCustomerId(account.getCustomerId())
                        .switchIfEmpty(Mono.error(new BusinessException("Customer not found")))
                )
                .map(customer -> new CustomerProfile(customer.getCustomerId(), customer.getName(), customer.getEmail()));

    }

    public Flux<Loan> getActiveLoans(String customerId) {
        // Caso de uso: Consultar todos los préstamos activos asociados al cliente especificado.
        return loanRepository.findByCustomerIdAndStatus(customerId,"activo");
    }

    public Flux<Double> simulateInterest(String accountId) {
        double rate = 0.05;

        // Caso de uso: Simular el interés compuesto en una cuenta bancaria. Sacar un rago de 10 años y aplicar la siguiente formula = principal * Math.pow(1 + rate, year)
        return accountRepository.findByAccountId(accountId)
                .flatMapMany(account -> {
                    double principal = account.getBalance();
                    return Flux.range(1, 10)
                            .map(year -> principal * Math.pow(1 + rate, year));
                })
                .doOnNext(interest -> System.out.println("Simulated interest: " + interest))
                .doOnError(error -> System.err.println("Error: " + error.getMessage()));
    }

    public Mono<String> getLoanStatus(String loanId) {
        // Caso de uso: Consultar el estado de un préstamo. se debe tener un flujo balanceMono y interestRateMono. Imprimir con el formato siguiente el resultado   "Loan ID: %s, Balance: %.2f, Interest Rate: %.2f%%"
        return Mono.zip(
                        loanRepository.findByLoanId(loanId).map(Loan::getBalance),
                        loanRepository.findByLoanId(loanId).map(Loan::getInterestRate)
                ).map(tuple -> {
                    System.out.println("Balance: " + tuple.getT1());
                    System.out.println("Interest Rate: " + tuple.getT2());
                    return String.format("Loan ID: %s, Balance: %.2f, Interest Rate: %.2f%%", loanId, tuple.getT1(), tuple.getT2() * 100);
                })
                .doOnNext(details -> System.out.println("Loan details: " + details))
                .doOnError(error -> System.err.println("Error: " + error.getMessage()));
    }

    public Mono<Account> getAccount(String idAccount) {
        return accountRepository.findByAccountId(idAccount);
    }

    public Mono<String> createLoad(Loan request) {
        // Caso de uso: Crear una nueva cuenta bancaria con un saldo inicial.
        Loan load = new Loan(request.getLoanId(), request.getBalance(), request.getInterestRate(),
                request.getCustomerId(),request.getStatus());
        return loanRepository.save(load)
                .map(savedLoad -> "Prestamo creada con ID: " + savedLoad.getLoanId());
    }

    public Mono<String> createCustomer(CustomerProfile request) {
        CustomerProfile customer = new CustomerProfile(request.getCustomerId(),request.getName(), request.getEmail());
        return customerRepository.save(customer)
                .map(savedCustomer ->"Cliente Creado con ID: " + savedCustomer.getCustomerId());
    }
}
