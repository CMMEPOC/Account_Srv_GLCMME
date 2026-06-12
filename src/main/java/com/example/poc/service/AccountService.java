package com.example.poc.service;

import com.example.poc.dto.AccountDetailsResponse;
import com.example.poc.entity.User;
import com.example.poc.repository.UserRepository;
import com.example.poc.dto.BalanceResponse;
import com.example.poc.entity.Account;
import com.example.poc.repository.AccountRepository;
import com.example.poc.repository.ValidationLogRepository;
import com.example.poc.entity.ValidationLog;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationLogRepository logRepository;

    public BalanceResponse validateBalance(
        String accountNumber,
        Double amount) {

    long startTime = System.currentTimeMillis();

    Account account =
            repository.findByAccountNumber(accountNumber)
            .orElse(null);

    ValidationLog log = new ValidationLog();

    log.setAccountNumber(accountNumber);
    log.setTransferAmount(amount);
    log.setValidationTime(LocalDateTime.now());

    if(account == null) {

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

    if(account.getCurrentBalance()
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
        String accountNumber) {

    Account account =
            repository.findByAccountNumber(accountNumber)
            .orElse(null);

    if (account == null) {
        return null;
    }

    User user =
            userRepository.findById(account.getUserId())
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
}