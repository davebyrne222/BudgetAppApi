package com.dave222.budgetapp.budget;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetModelAssembler budgetModelAssembler;

    public BudgetService(BudgetRepository budgetRepository, BudgetModelAssembler budgetModelAssembler) {
        this.budgetRepository = budgetRepository;
        this.budgetModelAssembler = budgetModelAssembler;
    }

    public EntityModel<Budget> create(Budget budget) {
        return budgetModelAssembler.toModel(budgetRepository.save(budget));
    }

    public List<EntityModel<Budget>> getAll() {
        return budgetRepository.findAll().stream()
                .map(budgetModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    public List<EntityModel<Budget>> getLatest() {
        return budgetRepository.findFirst5ByOrderByUpdatedDesc()
                .stream()
                .map(budgetModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Budget> getById(long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        return budgetModelAssembler.toModel(budget);
    }
}
