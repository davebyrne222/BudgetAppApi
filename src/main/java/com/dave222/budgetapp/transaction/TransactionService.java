package com.dave222.budgetapp.transaction;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionModelAssembler transactionModelAssembler;

    TransactionService(TransactionRepository transactionRepository, TransactionModelAssembler transactionModelAssembler) {

        this.transactionRepository = transactionRepository;
        this.transactionModelAssembler = transactionModelAssembler;
    }

    // Create
    public ResponseEntity<EntityModel<Transaction>> create(Long budgetId, @Valid Transaction transaction) {

        if (!budgetId.equals(transaction.getBudgetId())){
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Transaction newTransaction = transactionRepository.save(transaction);

        return ResponseEntity
                .created(linkTo(methodOn(TransactionController.class).getOne(budgetId, newTransaction.getId())).toUri())
                .body(transactionModelAssembler.toModel(newTransaction));
    }

    // Read
    public EntityModel<Transaction> getOne(Long budgetId, Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        return transactionModelAssembler.toModel(transaction);
    }

    public CollectionModel<EntityModel<Transaction>> getAll(Long budgetId) {

        List<EntityModel<Transaction>> transactions = transactionRepository.findAll().stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(transactions,
                linkTo(methodOn(TransactionController.class).getAll(budgetId)).withSelfRel());
    }

    // Update
    public ResponseEntity<?> update(Long budgetId, Long transactionId, @Valid Transaction newTransaction) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        // No difference between stored and new transaction
        if (transaction.equals(newTransaction)) {
            return ResponseEntity.noContent().build();
        }

        // Replace old transaction with new:
        newTransaction.setId(transaction.getId());

        return ResponseEntity.ok(transactionModelAssembler.toModel(transactionRepository.save(newTransaction)));

    }

    // Delete
    public ResponseEntity<?> delete(Long budgetId, Long transactionId) {

        if (!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException(transactionId);
        }

        transactionRepository.deleteById(transactionId);

        return ResponseEntity.ok("Transaction deleted");
    }
}
