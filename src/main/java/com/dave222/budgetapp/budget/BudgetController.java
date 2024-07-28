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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetRepository budgetRepository;
    private final BudgetModelAssembler budgetModelAssembler;
    private final TransactionService transactionService;
    private final BudgetService budgetService;

    BudgetController(BudgetRepository budgetRepository, BudgetModelAssembler budgetModelAssembler, TransactionService transactionService, BudgetService budgetService) {
        this.budgetRepository = budgetRepository;
        this.budgetModelAssembler = budgetModelAssembler;
        this.transactionService = transactionService;
        this.budgetService = budgetService;
    }

    // Create
    @PostMapping("/")
    ResponseEntity<EntityModel<Budget>> create(@Valid @RequestBody BudgetRequest budgetRequest) {

        EntityModel<Budget> newBudget = budgetModelAssembler.toModel(budgetService.create(budgetRequest));

        return ResponseEntity
                .created(linkTo(methodOn(BudgetController.class).getOne(newBudget.getContent().getId())).toUri())
                .body(newBudget);
    }

    // Read
    @GetMapping("/")
    CollectionModel<EntityModel<Budget>> all() {

        List<EntityModel<Budget>> budgets = budgetService.getAll();

        return CollectionModel.of(budgets,
                linkTo(methodOn(BudgetController.class).all()).withSelfRel());
    }

    @GetMapping("/latest")
    CollectionModel<EntityModel<Budget>> latest() {

        List<EntityModel<Budget>> budgets = budgetService.getLatest();

        return CollectionModel.of(budgets,
                linkTo(methodOn(BudgetController.class).latest()).withSelfRel());
    }

    @GetMapping("/{id}")
    ResponseEntity<EntityModel<Budget>> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getById(id));
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
        return ResponseEntity.ok(budgetService.update(id, newBudget));
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