package com.example.poc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "source_account_number")
    private String sourceAccountNumber;

    // Note: column name is intentionally misspelled to match the DB schema.
    @Column(name = "desitnation_account_number")
    private String destinationAccountNumber;

    @Column(name = "transaction_type")
    private String transactionType;

    private BigDecimal amount;

    @Column(name = "balance_after")
    private BigDecimal balanceAfter;

    @Column(name = "status")
    private String status;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
