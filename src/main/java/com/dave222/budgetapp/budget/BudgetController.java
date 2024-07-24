package com.dave222.budgetapp.budget;

import com.dave222.budgetapp.budget.enums.State;
import com.dave222.budgetapp.transaction.Transaction;
import com.dave222.budgetapp.transaction.TransactionService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    ResponseEntity<EntityModel<Budget>> create(@Valid @RequestBody Budget budget) {

        EntityModel<Budget> newBudget = budgetService.create(budget);

        return ResponseEntity
                .created(linkTo(methodOn(BudgetController.class).one(newBudget.getContent().getId())).toUri())
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

        List<EntityModel<Budget>> budgets = budgetRepository.findFirst5ByOrderByUpdatedDesc()
                .stream()
                .map(budgetModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(budgets,
                linkTo(methodOn(BudgetController.class).latest()).withSelfRel());
    }

    @GetMapping("/{id}")
    EntityModel<Budget> one(@PathVariable Long id) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        return budgetModelAssembler.toModel(budget);
    }

    @GetMapping("/{id}/transactions")
    CollectionModel<EntityModel<Transaction>> getTransactions(@PathVariable long id) {
        List<EntityModel<Transaction>> transactions = transactionService.getAllByBudget(id);

        // Todo: add delete and update links?
        return CollectionModel.of(transactions,
                linkTo(methodOn(BudgetController.class).getTransactions(id)).withSelfRel());
    }

    // Update
    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Budget newBudget) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        if (budget.getState() != State.ACTIVE){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Method Forbidden")
                            .withDetail("Budget of state " + budget.getState() + " can not be updated"));
        }

        // No difference between stored and new budgets
        if (budget.equals(newBudget)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(budgetModelAssembler.toModel(budgetRepository.save(newBudget)));

    }

    @PutMapping("/{id}/archive")
    ResponseEntity<?> archive(@PathVariable Long id) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        if (budget.getState() == State.ACTIVE) {
            budget.setState(State.ARCHIVED);
            budgetRepository.save(budget);
            return ResponseEntity.ok(budgetModelAssembler.toModel(budget));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method Not Allowed")
                        .withDetail("You can't archive a budget that is in the " + budget.getState() + " state"));
    }

    @PutMapping("/{id}/dearchive")
    ResponseEntity<?> dearchive(@PathVariable Long id) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        if (budget.getState() == State.ARCHIVED) {
            budget.setState(State.ACTIVE);
            budgetRepository.save(budget);
            return ResponseEntity.ok(budgetModelAssembler.toModel(budget));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method Not Allowed")
                        .withDetail("You can't de-archive a budget that is in the " + budget.getState() + " state"));
    }

    // Delete
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {

       if (!budgetRepository.existsById(id)) {
           throw new BudgetNotFoundException(id);
       }

       budgetRepository.deleteById(id);
        /*
        TODO: Delete transactions
        https://howtodoinjava.com/hibernate/hibernate-jpa-cascade-types/
        transactionRepository.deleteById(id);
        */

        return ResponseEntity.ok("Budget deleted");
    }}