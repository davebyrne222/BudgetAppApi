package com.dave222.budgetapp.budget;

public class BudgetNotActiveException extends RuntimeException {
    BudgetNotActiveException(Long id) {
        super("Invalid action: Budget " + id + " is not active.");
    }
}