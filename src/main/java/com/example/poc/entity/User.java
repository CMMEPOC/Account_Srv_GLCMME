package com.example.poc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "username")
    private String userName;

    private String email;

    private String phone;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}