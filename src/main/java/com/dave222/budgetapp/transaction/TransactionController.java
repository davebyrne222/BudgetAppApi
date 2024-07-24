package com.dave222.budgetapp.transaction;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/transactions")
class TransactionController {

    private final TransactionService transactionService;
    private final TransactionModelAssembler transactionModelAssembler;


    TransactionController(TransactionService transactionService, TransactionModelAssembler transactionModelAssembler) {
        this.transactionService = transactionService;
        this.transactionModelAssembler = transactionModelAssembler;
    }

    // Create
    @PostMapping("/")
    ResponseEntity<EntityModel<Transaction>> create(@RequestBody @Valid Transaction transaction) {
         Transaction newTransaction = transactionService.create(transaction);

        return ResponseEntity
                .created(linkTo(methodOn(TransactionController.class).getOne(newTransaction.getId())).toUri())
                .body(transactionModelAssembler.toModel(newTransaction));
    }

    // Read
    @GetMapping("/")
    CollectionModel<EntityModel<Transaction>> getAll() {
        List<EntityModel<Transaction>> transactions = transactionService.getAll();

        return CollectionModel.of(transactions,
                linkTo(methodOn(TransactionController.class).getAll()).withSelfRel());
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