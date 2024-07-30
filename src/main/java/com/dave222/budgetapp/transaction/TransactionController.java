package com.dave222.budgetapp.transaction;

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
         EntityModel<Transaction> newTransaction = transactionModelAssembler.toModel(transactionService.create(transaction));

        return ResponseEntity
                .created(linkTo(methodOn(TransactionController.class).getOne(Objects.requireNonNull(newTransaction.getContent()).getId())).toUri())
                .body(newTransaction);
    }

    // Read
    @GetMapping("/")
    CollectionModel<EntityModel<Transaction>> getAll() {
        List<EntityModel<Transaction>> transactions =
                transactionService.getAll()
                .stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(transactions,
                linkTo(methodOn(TransactionController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{transactionId}")
    ResponseEntity<EntityModel<Transaction>> getOne(@PathVariable Long transactionId) {
        return ResponseEntity
                .ok(transactionModelAssembler.toModel(
                        transactionService.getOne(transactionId)
                ));
    }

    // Update
    @PutMapping("/{transactionId}")
    ResponseEntity<EntityModel<Transaction>> update(@PathVariable Long transactionId, @RequestBody @Valid Transaction newTransaction) {
        return ResponseEntity.ok(
                transactionModelAssembler.toModel(
                    transactionService.update(transactionId, newTransaction)
                ));
    }

    // Delete
    @DeleteMapping("/{transactionId}")
    ResponseEntity<?> delete(@PathVariable Long transactionId) {
        return ResponseEntity.ok(transactionService.delete(transactionId));
    }}