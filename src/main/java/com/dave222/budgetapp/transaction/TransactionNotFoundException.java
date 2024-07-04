package com.dave222.budgetapp.transaction;

class TransactionNotFoundException extends RuntimeException {
    TransactionNotFoundException(Long id) {
        super("Could not find transaction " + id);
    }
}