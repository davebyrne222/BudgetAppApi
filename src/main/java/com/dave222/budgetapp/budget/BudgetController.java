package com.dave222.budgetapp.budget;

import com.dave222.budgetapp.budget.enums.State;
import com.dave222.budgetapp.transaction.Transaction;
import com.dave222.budgetapp.transaction.TransactionService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetModelAssembler budgetModelAssembler;
    private final TransactionService transactionService;
    private final BudgetService budgetService;

    BudgetController(BudgetModelAssembler budgetModelAssembler, TransactionService transactionService, BudgetService budgetService) {
        this.budgetModelAssembler = budgetModelAssembler;
        this.transactionService = transactionService;
        this.budgetService = budgetService;
    }

    // Create
    @PostMapping("/")
    ResponseEntity<EntityModel<Budget>> create(@Valid @RequestBody BudgetRequest budgetRequest) {

        EntityModel<Budget> newBudget = budgetModelAssembler.toModel(budgetService.create(budgetRequest));

        return ResponseEntity
                .created(linkTo(methodOn(BudgetController.class).getOne(Objects.requireNonNull(newBudget.getContent()).getId())).toUri())
                .body(newBudget);
    }

    // Read
    @GetMapping("/")
    CollectionModel<EntityModel<Budget>> all() {

        List<EntityModel<Budget>> budgets = budgetService.getAll()
                .stream()
                .map(budgetModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(budgets,
                linkTo(methodOn(BudgetController.class).all()).withSelfRel());
    }

    @GetMapping("/latest")
    CollectionModel<EntityModel<Budget>> latest() {

        List<EntityModel<Budget>> budgets = budgetService.getLatest()
                .stream()
                .map(budgetModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(budgets,
                linkTo(methodOn(BudgetController.class).latest()).withSelfRel());
    }

    @GetMapping("/{id}")
    ResponseEntity<EntityModel<Budget>> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(
                budgetModelAssembler.toModel(
                        budgetService.getById(id)
                ));
    }

    @GetMapping("/{id}/transactions")
    CollectionModel<EntityModel<Transaction>> getTransactions(@PathVariable long id) {
        List<EntityModel<Transaction>> transactions = transactionService.getAllByBudget(id);

        return CollectionModel.of(transactions,
                linkTo(methodOn(BudgetController.class).getTransactions(id)).withSelfRel());
    }

    // Update
    @PutMapping("/{id}")
    ResponseEntity<EntityModel<Budget>> update(@PathVariable Long id, @Valid @RequestBody BudgetRequest newBudget) {
        EntityModel<Budget> budget = budgetModelAssembler.toModel(budgetService.update(id, newBudget));
        return ResponseEntity.ok(budget);
    }

    @PutMapping("/{id}/archive")
    ResponseEntity<String> archive(@PathVariable Long id) {
        budgetService.setState(id, State.ARCHIVED);
        return ResponseEntity.ok("Budget archived");
    }

    @PutMapping("/{id}/dearchive")
    ResponseEntity<String> dearchive(@PathVariable Long id) {
        budgetService.setState(id, State.ACTIVE);
        return ResponseEntity.ok("Budget de-archived");
    }

    // Delete
    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        budgetService.deleteById(id);
        return ResponseEntity.ok("Budget deleted");
    }}