package com.dave222.budgetapp.common.exceptions;

public class RedundantRequestException extends RuntimeException {
    public RedundantRequestException(String message) {
        super(message);
    }
}