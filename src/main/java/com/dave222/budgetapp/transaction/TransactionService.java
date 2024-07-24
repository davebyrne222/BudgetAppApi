package com.dave222.budgetapp.transaction;

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
    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // Read
    public EntityModel<Transaction> getOne(Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        return transactionModelAssembler.toModel(transaction);
    }

    public CollectionModel<EntityModel<Transaction>> getAll() {

        List<EntityModel<Transaction>> transactions = transactionRepository.findAll()
                .stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(transactions,
                linkTo(methodOn(TransactionController.class).getAll()).withSelfRel());
    }

    public List<EntityModel<Transaction>> getAllByBudget(Long budgetId) {

        return transactionRepository.findByBudgetId(budgetId)
                .stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    // Update
    public ResponseEntity<?> update(Long transactionId, Transaction newTransaction) {

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
    public ResponseEntity<?> delete(Long transactionId) {

        if (!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException(transactionId);
        }

        transactionRepository.deleteById(transactionId);

        return ResponseEntity.ok("Transaction deleted");
    }
}
