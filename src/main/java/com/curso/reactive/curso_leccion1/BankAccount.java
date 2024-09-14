package com.curso.reactive.curso_leccion1;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BankAccount {
    private List<Transaction> transactions;

    public BankAccount() {
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    // TODO 1: Implementar getTotalBalance utilizando streams y reduce
    public Optional<Double> getTotalBalance() {
        return transactions.stream().map( transaction ->{
                    return transaction.getType().equals("deposit") ? transaction.getAmount() : -transaction.getAmount();
                }).reduce(Double::sum);

    }

    // TODO 2: Implementar getDeposits utilizando streams y filter
    public Optional<List<Transaction>> getDeposits() {
        List<Transaction> deposit = transactions.stream()
                .filter(transaction -> "deposit".equals(transaction.getType()))
                .collect(Collectors.toList());
        return Optional.ofNullable(deposit);
    }

    // TODO 3: Implementar getWithdrawals utilizando streams y filter
    public Optional<List<Transaction>> getWithdrawals() {
        List<Transaction> deposit = transactions.stream()
                .filter(transaction -> "withdrawal".equals(transaction.getType()))
                .collect(Collectors.toList());
        return Optional.ofNullable(deposit);
    }

    // TODO 4: Implementar filterTransactions utilizando Function y streams
    public Optional<List<Transaction>> filterTransactions(Function<Transaction, Boolean> predicate) {
        List<Transaction> transactionList = transactions.stream()
                .filter(predicate::apply)
                .collect(Collectors.toList());
        return Optional.ofNullable(transactionList);
    }

    // TODO 5: Implementar getTotalDeposits utilizando getDeposits y mapToDouble
    public Optional<Double> getTotalDeposits() {
        // TODO 5
        return getDeposits().map(deposit -> deposit.stream().mapToDouble(trx -> trx.getAmount()).sum());
    }

    // TODO 6: Implementar getLargestWithdrawal utilizando getWithdrawals y max
    public Optional<Transaction> getLargestWithdrawal() {
        // TODO 6
        return getWithdrawals().get().stream()
                .max((trx1,trx2)->Double.compare(trx1.getAmount(),trx2.getAmount()));
    }

    // TODO 7: Implementar getTransactionsOnDate utilizando streams y filter
    public Optional<List<Transaction>> getTransactionsOnDate(String date) {
        List<Transaction> deposit = transactions.stream()
                .filter(transaction -> date.equals(transaction.getDate()))
                .collect(Collectors.toList());
        return Optional.ofNullable(deposit);
    }

    // TODO 8: Implementar getAverageTransactionAmount utilizando streams y mapToDouble
    public OptionalDouble getAverageTransactionAmount() {
        // TODO 8
        return transactions.stream().mapToDouble(trx->trx.getAmount()).average();
    }

    // TODO 9: Implementar getTransactionsWithAmountGreaterThan utilizando streams y filter
    public Optional<List<Transaction>> getTransactionsWithAmountGreaterThan(double amount) {
        List<Transaction> filteredTransactions = transactions.stream()
                .filter(transaction -> transaction.getAmount() > amount)
                .collect(Collectors.toList());

        return filteredTransactions.isEmpty() ? Optional.empty() : Optional.of(filteredTransactions);
    }

    // TODO 10: Implementar transfer utilizando addTransaction
    public static void transfer(BankAccount sourceAccount, BankAccount targetAccount, double amount) {
        if (sourceAccount.getTotalBalance().get() >= amount) {
            String date = new  Date().toString();
            sourceAccount.addTransaction(new Transaction(amount,"withdrawal",date ));
            targetAccount.addTransaction(new Transaction(amount,"deposit",date ));
        } else {
            throw new IllegalArgumentException("Insufficient funds in source account.");
        }
    }

    // TODO 11: Implementar getTotalWithdrawals utilizando getWithdrawals y mapToDouble
    public Optional<Double> getTotalWithdrawals() {
        List<Transaction> withdrawals = getWithdrawals().get();

        if (withdrawals.isEmpty()) {
            return Optional.empty();
        }

        double totalWithdrawals = withdrawals.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        return Optional.of(totalWithdrawals);
    }

    // TODO 12: Implementar getTransactionsSummary utilizando streams, map y collect
    public Map<String, Double> getTransactionsSummary() {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getType,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }
    /*
	Descripción de los TODOs:
			1. getTotalBalance: Implementar el cálculo del saldo total utilizando map y reduce.
			2. getDeposits: Implementar la obtención de todas las transacciones de tipo "deposit" utilizando filter.
			3. getWithdrawals: Implementar la obtención de todas las transacciones de tipo "withdrawal" utilizando filter.
			4. filterTransactions: Implementar un método genérico para filtrar transacciones utilizando una función Function.
			5. getTotalDeposits: Implementar el cálculo del total de depósitos utilizando mapToDouble y sum.
			6. getLargestWithdrawal: Implementar la búsqueda del retiro de mayor monto utilizando max.
			7. getTransactionsOnDate: Implementar la obtención de transacciones en una fecha específica utilizando filter.
			8. getAverageTransactionAmount: Implementar el cálculo del monto promedio de las transacciones utilizando mapToDouble y average.
			9. getTransactionsWithAmountGreaterThan: Implementar la obtención de transacciones con monto mayor a un valor específico utilizando filter.
			10. transfer: Implementar la transferencia de fondos entre cuentas utilizando addTransaction.
			11. getTotalWithdrawals: Implementar el cálculo del total de retiros utilizando mapToDouble y sum.
			12. getTransactionsSummary: Implementar un resumen de transacciones agrupado por tipo utilizando collect y groupingBy.
			*/
}
