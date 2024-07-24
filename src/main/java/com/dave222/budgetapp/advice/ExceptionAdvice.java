package com.dave222.budgetapp.advice;

import com.dave222.budgetapp.budget.BudgetIdenticalException;
import com.dave222.budgetapp.budget.BudgetNotActiveException;
import com.dave222.budgetapp.budget.BudgetNotFoundException;
import com.dave222.budgetapp.transaction.TransactionIdenticalException;
import com.dave222.budgetapp.transaction.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({
            BudgetNotFoundException.class,
            TransactionNotFoundException.class
    })
    public ResponseEntity<String> handleNotFoundException(BudgetNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({
            BudgetNotActiveException.class
    })
    public ResponseEntity<String> handleNotActiveException(BudgetNotActiveException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler({
            BudgetIdenticalException.class,
            TransactionIdenticalException.class
    })
    public ResponseEntity<String> handleNoUpdateException(TransactionIdenticalException ex) {
        return ResponseEntity.noContent().build();
    }
}