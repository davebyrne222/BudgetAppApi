package com.dave222.budgetapp.budget;

import com.dave222.budgetapp.budget.enums.State;
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

    public EntityModel<Budget> update(long id, Budget newBudget) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        if (budget.getState() != State.ACTIVE) throw new BudgetNotActiveException(id);

        if (budget.equals(newBudget)) throw new BudgetIdenticalException();

        return budgetModelAssembler.toModel(budgetRepository.save(newBudget));
    }

    /**
     * Sets the state of the budget with the given id.
     * <p>
     * This method retrieves the budget with the specified id from the repository.
     * If the budget does not exist, a {@link BudgetNotFoundException} is thrown.
     * If the budget is already in the specified state, a {@link RedundantRequestException} is thrown.
     * Otherwise, the budget's state is updated to the provided state and the updated budget
     * is saved back to the repository.
     * </p>
     *
     * @param id the id of the budget to update
     * @param state the new state to set for the budget
     * @throws BudgetNotFoundException if no budget with the given id is found
     * @throws RedundantRequestException if the budget is already in the specified state
     */
    public void setState(long id, State state) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        if (budget.getState() == state) throw new RedundantRequestException("Budget is already in state: " + state.name());

        budget.setState(state);

        budgetRepository.save(budget);
    }
}
