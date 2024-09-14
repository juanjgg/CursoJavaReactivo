package com.curso.reactive.domain;

public class Loan {//pertamo
    private String loanId;
    private Double balance;
    private Double interestRate;
    private String customerId;
    private  String status;

    public Loan(String loanId, Double balance, Double interestRate, String customerId, String status) {
        this.loanId = loanId;
        this.balance = balance;
        this.interestRate = interestRate;
        this.customerId = customerId;
        this.status = status;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
