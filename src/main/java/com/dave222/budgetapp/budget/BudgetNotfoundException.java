package com.dave222.budgetapp.budget;

class BudgetNotFoundException extends RuntimeException {
    BudgetNotFoundException(Long id) {
        super("Could not find budget " + id);
    }
}