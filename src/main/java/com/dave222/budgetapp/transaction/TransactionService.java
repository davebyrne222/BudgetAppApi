package com.dave222.budgetapp.transaction;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionModelAssembler transactionModelAssembler;

    TransactionService(TransactionRepository transactionRepository, TransactionModelAssembler transactionModelAssembler) {

        this.transactionRepository = transactionRepository;
        this.transactionModelAssembler = transactionModelAssembler;
    }

    // Create
    public EntityModel<Transaction> create(Transaction transaction) {
        return transactionModelAssembler.toModel(transactionRepository.save(transaction));
    }

    // Read
    public List<EntityModel<Transaction>> getAll() {
        return transactionRepository.findAll()
                .stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    public EntityModel<Transaction> getOne(Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        return transactionModelAssembler.toModel(transaction);
    }

    public List<EntityModel<Transaction>> getAllByBudget(Long budgetId) {

        return transactionRepository.findByBudgetId(budgetId)
                .stream()
                .map(transactionModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    // Update
    public EntityModel<Transaction> update(Long transactionId, Transaction newTransaction) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        // No difference between stored and new transaction
        if (transaction.equals(newTransaction)) throw new TransactionIdenticalException();


        // Replace old transaction with new:
        newTransaction.setId(transaction.getId());

        return transactionModelAssembler.toModel(transactionRepository.save(newTransaction));

    }

    // Delete
    public String delete(Long transactionId) {

        if (!transactionRepository.existsById(transactionId)) throw new TransactionNotFoundException(transactionId);

        transactionRepository.deleteById(transactionId);

        return "Transaction deleted";
    }
}
