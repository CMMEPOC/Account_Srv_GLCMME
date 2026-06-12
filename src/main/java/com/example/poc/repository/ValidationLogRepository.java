package com.example.poc.repository;

import com.example.poc.entity.ValidationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationLogRepository
        extends JpaRepository<ValidationLog, Long> {
}