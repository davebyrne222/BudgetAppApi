package com.dave222.budgetapp.budget;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findFirst5ByOrderByUpdatedDesc();
}