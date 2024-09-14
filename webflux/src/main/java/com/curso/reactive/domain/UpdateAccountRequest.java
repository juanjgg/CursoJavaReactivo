package com.curso.reactive.domain;

public class UpdateAccountRequest {
    private String accountId;
    private Double newBalance;
    private String newCustomerId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Double newBalance) {
        this.newBalance = newBalance;
    }

    public String getNewCustomerId() {
        return newCustomerId;
    }

    public void setNewCustomerId(String newCustomerId) {
        this.newCustomerId = newCustomerId;
    }
}
