package com.example.poc.controller;

import com.example.poc.dto.AccountDetailsResponse;
import com.example.poc.dto.BalanceResponse;
import com.example.poc.service.AccountService;

import java.util.UUID;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/validate")
    public BalanceResponse validateBalance(
            @RequestParam String accountNumber,
            @RequestParam Double amount) {

        return service.validateBalance(
                accountNumber,
                amount);
    }

    @GetMapping("/accountDetails/{userId}")
    public AccountDetailsResponse getAccountDetails(
            @PathVariable UUID userId) {

        return service.getAccountDetails(userId);
    }
}