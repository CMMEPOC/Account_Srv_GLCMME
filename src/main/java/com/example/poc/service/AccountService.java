package com.example.poc.service;

import com.example.poc.dto.response.AccountDetailsResponse;
import com.example.poc.entity.User;
import com.example.poc.repository.UserRepository;
import com.example.poc.dto.response.BalanceResponse;
import com.example.poc.dto.response.AccountStatementResponse;
import com.example.poc.dto.response.TransactionResponse;
import com.example.poc.entity.Account;
import com.example.poc.entity.Transaction;
import com.example.poc.repository.AccountRepository;
import com.example.poc.repository.TransactionRepository;
import com.example.poc.repository.ValidationLogRepository;
import com.example.poc.entity.ValidationLog;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationLogRepository logRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public BalanceResponse validateBalance(
            String accountNumber,
            Double amount) {

        long startTime = System.currentTimeMillis();

        Account account =
                accountRepository.findByAccountNumber(accountNumber)
                        .orElse(null);

        ValidationLog log = new ValidationLog();

        log.setAccountNumber(accountNumber);
        log.setTransferAmount(amount);
        log.setValidationTime(LocalDateTime.now());

        if (account == null) {

            long responseTime =
                    System.currentTimeMillis() - startTime;

            log.setValidationStatus("REJECTED");
            log.setMessage("Account Not Found");
            log.setResponseTimeMs(responseTime);

            logRepository.save(log);

            return new BalanceResponse(
                    false,
                    "Account Not Found");
        }

        if (account.getCurrentBalance()
                .doubleValue() >= amount) {

            long responseTime =
                    System.currentTimeMillis() - startTime;

            log.setValidationStatus("APPROVED");
            log.setMessage("Transfer Approved");
            log.setResponseTimeMs(responseTime);

            logRepository.save(log);

            return new BalanceResponse(
                    true,
                    "Transfer Approved");
        }

        long responseTime =
                System.currentTimeMillis() - startTime;

        log.setValidationStatus("REJECTED");
        log.setMessage("Insufficient Balance");
        log.setResponseTimeMs(responseTime);

        logRepository.save(log);

        return new BalanceResponse(
                false,
                "Insufficient Balance");

    }

    public AccountDetailsResponse getAccountDetails(
            UUID userId) {

        Account account =
                accountRepository.findByUserId(userId)
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
                user.getUsername(),
                account.getAccountNumber(),
                account.getCurrentBalance(),
                account.getIfsc(),
                account.getBankName(),
                account.getAccountType()
        );
    }

    public AccountStatementResponse getAccountStatement(String accountId) {

        Account account =
                accountRepository.findById(accountId)
                        .orElse(null);

        if (account == null) {
            return null;
        }

        List<Transaction> transactions = null;
        transactions = transactionRepository.findStatementByAccountNumber(
                        account.getAccountNumber());
        if(transactions.isEmpty()){
            return new AccountStatementResponse(account.getCurrentBalance(), null);
        }

        List<TransactionResponse> sortedByTimeTransactions =
                transactions.stream()
                        .map(t -> new TransactionResponse(
                                t.getTransactionId(),
                                t.getSourceAccountNumber(),
                                t.getDestinationAccountNumber(),
                                t.getTransactionType(),
                                t.getAmount(),
                                t.getBalanceAfter(),
                                toEpochSeconds(t.getCreatedAt())))
                        .collect(Collectors.toList());

        return new AccountStatementResponse(
                account.getCurrentBalance(),
                sortedByTimeTransactions);
    }

    private Double toEpochSeconds(LocalDateTime timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toInstant(ZoneOffset.UTC).toEpochMilli() / 1000.0;
    }
}