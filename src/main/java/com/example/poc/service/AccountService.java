package com.example.poc.service;

import com.example.poc.dto.AccountDetailsResponse;
import com.example.poc.dto.BalanceResponse;
import com.example.poc.entity.Account;
import com.example.poc.entity.User;
import com.example.poc.repository.AccountRepository;
import com.example.poc.repository.UserRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final UserRepository userRepository;

    public AccountService(
            AccountRepository repository,
            UserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    public BalanceResponse validateBalance(
            String accountNumber,
            Double amount) {

        Account account =
                repository.findByAccountNumber(accountNumber)
                        .orElse(null);

        if (account == null) {
            return new BalanceResponse(
                    false,
                    "Account Not Found");
        }

        if (account.getCurrentBalance().doubleValue() >= amount) {
            return new BalanceResponse(
                    true,
                    "Transfer Approved");
        }

        return new BalanceResponse(
                false,
                "Insufficient Balance");
    }

    public AccountDetailsResponse getAccountDetails(
            UUID userId) {

        Account account =
                repository.findByUserId(userId)
                        .orElse(null);

        if (account == null) {
            return null;
        }

        User user =
                userRepository.findById(userId)
                        .orElse(null);

        if (user == null) {
            return null;
        }

        return new AccountDetailsResponse(
                user.getUserName(),
                account.getAccountNumber(),
                account.getCurrentBalance(),
                account.getIfsc(),
                account.getBankName(),
                account.getAccountType());
    }
}