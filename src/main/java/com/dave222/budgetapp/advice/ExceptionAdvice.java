package com.dave222.budgetapp.advice;

import com.dave222.budgetapp.transaction.TransactionIdenticalException;
import com.dave222.budgetapp.transaction.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<String> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /*
    Usage: When an update results in no change
     */
    @ExceptionHandler(TransactionIdenticalException.class)
    public ResponseEntity<String> handleTransactionIdenticalException(TransactionIdenticalException ex) {
        return ResponseEntity.noContent().build();
    }
}