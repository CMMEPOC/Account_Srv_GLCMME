package com.example.poc.controller;

import com.example.poc.dto.AccountDetailsResponse;
import com.example.poc.dto.BalanceResponse;
import com.example.poc.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @GetMapping("/validate")
    public BalanceResponse validateBalance(
            @RequestParam String accountNumber,
            @RequestParam Double amount) {

        return service.validateBalance(
                accountNumber,
                amount);
    }

    @GetMapping("/details/{accountNumber}")
    public AccountDetailsResponse getAccountDetails(
        @PathVariable String accountNumber) {

        return service.getAccountDetails(accountNumber);
    }
}