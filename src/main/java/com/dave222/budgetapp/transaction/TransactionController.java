package com.dave222.budgetapp.transaction;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/transactions")
class TransactionController {

    private final TransactionService transactionService;

    TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Create
    @PostMapping("/")
    ResponseEntity<EntityModel<Transaction>> create(@RequestBody @Valid Transaction transaction) {
        return transactionService.create(transaction);
    }

    // Read
    @GetMapping("/")
    CollectionModel<EntityModel<Transaction>> getAll() {
        return transactionService.getAll();
    }

    @GetMapping("/{transactionId}")
    EntityModel<Transaction> getOne(@PathVariable Long transactionId) {
        return transactionService.getOne(transactionId);
    }

    // Update
    @PutMapping("/{transactionId}")
    ResponseEntity<?> update(@PathVariable Long transactionId, @RequestBody @Valid Transaction newTransaction) {
        return transactionService.update(transactionId, newTransaction);
    }

    // Delete
    @DeleteMapping("/{transactionId}")
    ResponseEntity<?> delete(@PathVariable Long transactionId) {
        return transactionService.delete(transactionId);
    }}