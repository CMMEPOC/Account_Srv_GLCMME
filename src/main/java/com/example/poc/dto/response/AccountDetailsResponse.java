package com.example.poc.dto.response;

import java.math.BigDecimal;

public class AccountDetailsResponse {

    private String username;
    private String accountNumber;
    private BigDecimal balance;
    private String ifsc;
    private String bankName;
    private String accountType;

    public AccountDetailsResponse(
            String username,
            String accountNumber,
            BigDecimal balance,
            String ifsc,
            String bankName,
            String accountType) {

        this.username = username;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.ifsc = ifsc;
        this.bankName = bankName;
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getIfsc() {
        return ifsc;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountType() {
        return accountType;
    }
}