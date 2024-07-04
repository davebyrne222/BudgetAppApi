package com.dave222.budgetapp.transaction;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionModelAssembler transactionModelAssembler;

    TransactionController(TransactionRepository transactionRepository, TransactionModelAssembler transactionModelAssembler) {

        this.transactionRepository = transactionRepository;
        this.transactionModelAssembler = transactionModelAssembler;
    }

    // Create
    @PostMapping("/transactions")
    ResponseEntity<EntityModel<Transaction>> newBudget(@RequestBody Transaction transaction) {

        Transaction newTransaction = transactionRepository.save(transaction);

        return ResponseEntity
                .created(linkTo(methodOn(TransactionController.class).one(newTransaction.getId())).toUri())
                .body(transactionModelAssembler.toModel(newTransaction));
    }

    // Read
    @GetMapping("/transactions")
    CollectionModel<EntityModel<Transaction>> all() {

        List<EntityModel<Transaction>> transactions = transactionRepository.findAll().stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(transactions,
                linkTo(methodOn(TransactionController.class).all()).withSelfRel());
    }

    @GetMapping("/transactions/{id}")
    EntityModel<Transaction> one(@PathVariable Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        return transactionModelAssembler.toModel(transaction);
    }

    // Update
    @PutMapping("/transactions/{id}")
    ResponseEntity<?> update(@RequestBody Transaction newTransaction, @PathVariable Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        // No difference between stored and new transaction
        if (transaction.equals(newTransaction)) {
            return ResponseEntity.noContent().build();
        }

        // Replace old transaction with new:
        newTransaction.setId(transaction.getId());

        return ResponseEntity.ok(transactionModelAssembler.toModel(transactionRepository.save(newTransaction)));

    }

    // Delete
    @DeleteMapping("/transactions/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {

       if (!transactionRepository.existsById(id)) {
           throw new TransactionNotFoundException(id);
       }

       transactionRepository.deleteById(id);
        /*
        TODO: Delete transactions
        https://howtodoinjava.com/hibernate/hibernate-jpa-cascade-types/
        transactionRepository.deleteById(id);
        */

        return ResponseEntity.ok("Transaction deleted");
    }}