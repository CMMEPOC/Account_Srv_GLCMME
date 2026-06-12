package com.example.poc.dto;

public class BalanceResponse {

    private boolean valid;

    private String message;

    public BalanceResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}