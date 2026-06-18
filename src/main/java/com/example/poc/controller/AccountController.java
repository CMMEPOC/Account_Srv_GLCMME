package com.example.poc.controller;

import com.example.poc.dto.response.AccountDetailsResponse;
import com.example.poc.dto.response.AccountStatementResponse;
import com.example.poc.dto.response.BalanceResponse;
import com.example.poc.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
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

    @GetMapping("/details/{userId}")
    public AccountDetailsResponse getAccountDetails(
        @PathVariable UUID userId) {

        return service.getAccountDetails(userId);
    }
    /*
    Account Stmt Api:
    GET /api/v1/accounts/{account_id}/stmt
    Headers:
        Basic Auth Bearer Token
    Response:
    {
        "currentBalance" : 90000,
        "sortedByTimeTransactions" : [
            {
                "transactionId": 123,
                "srcAccNo": 123,
                "destAccNo": 123,
                "type": "CREDIT",
                "amount": 2000.00,
                "netBalance": 86000.00,
                "timeStamp": 1212212.2
            },
            {
                "transactionId": 124,
                "srcAccNo": 123,
                "destAccNo": 123,
                "type": "DEBIT",
                "amount": 2000.00,
                "netBalance": 84000.00,
                "timeStamp": 1212220.2
            },
    }
    */

    @GetMapping("/{account_id}/stmt")
    public ResponseEntity<AccountStatementResponse> getAccountStatement(
            @PathVariable(name = "account_id") String accountId) {
        AccountStatementResponse accountStatementResponse = service.getAccountStatement(accountId);

        return new ResponseEntity<>(accountStatementResponse, HttpStatus.OK);
    }


}