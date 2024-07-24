package com.dave222.budgetapp.budget;

public class BudgetIdenticalException extends RuntimeException {
    BudgetIdenticalException() {
        super("The provided body results in no update");
    }
}