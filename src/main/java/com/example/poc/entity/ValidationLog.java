package com.example.poc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="validation_log")
public class ValidationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private Double transferAmount;

    private String validationStatus;

    private String message;

    private LocalDateTime validationTime;

    private Long responseTimeMs;

    // Generate getters and setters
}