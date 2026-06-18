package com.example.poc.repository;

import com.example.poc.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM Transaction t "
            + "WHERE t.sourceAccountNumber = :accountNumber "
            + "   OR t.destinationAccountNumber = :accountNumber "
            + "ORDER BY t.createdAt ASC")
    List<Transaction> findStatementByAccountNumber(
            @Param("accountNumber") String accountNumber);
}
