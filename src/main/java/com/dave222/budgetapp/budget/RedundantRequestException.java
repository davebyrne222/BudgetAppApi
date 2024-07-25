package com.dave222.budgetapp.budget;

public class RedundantRequestException extends RuntimeException {
    public RedundantRequestException(String message) {
        super(message);
    }
}