package com.example.poc;

import com.example.poc.dto.AccountDetailsResponse;
import com.example.poc.dto.BalanceResponse;
import com.example.poc.entity.Account;
import com.example.poc.entity.User;
import com.example.poc.repository.AccountRepository;
import com.example.poc.repository.UserRepository;
import com.example.poc.service.AccountService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PocApplicationTests {

@Mock
private AccountRepository accountRepository;

@Mock
private UserRepository userRepository;

@InjectMocks
private AccountService accountService;

public PocApplicationTests() {
    MockitoAnnotations.openMocks(this);
}

@Test
void validateBalanceSuccess() {

    Account account = new Account();
    account.setAccountNumber("987654321098");
    account.setCurrentBalance(new BigDecimal("50000"));

    when(accountRepository.findByAccountNumber("987654321098"))
            .thenReturn(Optional.of(account));

    BalanceResponse response =
            accountService.validateBalance(
                    "987654321098",
                    1000.0);

    assertTrue(response.isValid());
    assertEquals(
            "Transfer Approved",
            response.getMessage());
}

@Test
void validateBalanceInsufficientBalance() {

    Account account = new Account();
    account.setAccountNumber("987654321098");
    account.setCurrentBalance(new BigDecimal("500"));

    when(accountRepository.findByAccountNumber("987654321098"))
            .thenReturn(Optional.of(account));

    BalanceResponse response =
            accountService.validateBalance(
                    "987654321098",
                    1000.0);

    assertFalse(response.isValid());
    assertEquals(
            "Insufficient Balance",
            response.getMessage());
}

@Test
void validateBalanceAccountNotFound() {

    when(accountRepository.findByAccountNumber("111111"))
            .thenReturn(Optional.empty());

    BalanceResponse response =
            accountService.validateBalance(
                    "111111",
                    1000.0);

    assertFalse(response.isValid());
    assertEquals(
            "Account Not Found",
            response.getMessage());
}

@Test
void getAccountDetailsSuccess() {

    UUID userId = UUID.randomUUID();

    User user = new User();
    user.setUserId(userId);
    user.setUserName("user1");

    Account account = new Account();
    account.setUserId(userId);
    account.setAccountNumber("987654321098");
    account.setCurrentBalance(
            new BigDecimal("50000"));
    account.setIfsc("MPHA0001234");
    account.setBankName("Mphasis LTD");
    account.setAccountType("SAVINGS");

    when(accountRepository.findByUserId(userId))
            .thenReturn(Optional.of(account));

    when(userRepository.findById(userId))
            .thenReturn(Optional.of(user));

    AccountDetailsResponse response =
            accountService.getAccountDetails(
                    userId);

    assertNotNull(response);
    assertEquals(
            "user1",
            response.getUsername());
}

@Test
void getAccountDetailsAccountNotFound() {

    UUID userId = UUID.randomUUID();

    when(accountRepository.findByUserId(userId))
            .thenReturn(Optional.empty());

    AccountDetailsResponse response =
            accountService.getAccountDetails(
                    userId);

    assertNull(response);
}

}