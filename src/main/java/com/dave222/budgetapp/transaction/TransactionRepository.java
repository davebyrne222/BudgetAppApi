package com.dave222.budgetapp.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBudgetId(long budgetId);
}