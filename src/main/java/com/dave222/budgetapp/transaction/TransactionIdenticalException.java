package com.dave222.budgetapp.transaction;

public class TransactionIdenticalException extends RuntimeException {
    TransactionIdenticalException() {
        super("The provided body results in no update");
    }
}