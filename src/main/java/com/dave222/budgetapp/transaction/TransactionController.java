package com.dave222.budgetapp.transaction;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/budget/{budgetId}/transactions")
class TransactionController {

    private final TransactionService transactionService;

    TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Create
    @PostMapping("/")
    ResponseEntity<EntityModel<Transaction>> create(@PathVariable Long budgetId, @RequestBody @Valid Transaction transaction) {
        return transactionService.create(budgetId, transaction);
    }

    // Read
    @GetMapping("/")
    CollectionModel<EntityModel<Transaction>> getAll(@PathVariable Long budgetId) {
        return transactionService.getAll(budgetId);
    }

    @GetMapping("/{transactionId}")
    EntityModel<Transaction> getOne(@PathVariable Long budgetId, @PathVariable Long transactionId) {
        return transactionService.getOne(budgetId, transactionId);
    }

    // Update
    @PutMapping("/{transactionId}")
    ResponseEntity<?> update(@PathVariable Long budgetId, @PathVariable Long transactionId, @RequestBody @Valid Transaction newTransaction) {
        return transactionService.update(budgetId, transactionId, newTransaction);
    }

    // Delete
    @DeleteMapping("/{transactionId}")
    ResponseEntity<?> delete(@PathVariable Long budgetId, @PathVariable Long transactionId) {
        return transactionService.delete(budgetId, transactionId);
    }}