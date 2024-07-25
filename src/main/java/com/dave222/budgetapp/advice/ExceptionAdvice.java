package com.dave222.budgetapp.advice;

import com.dave222.budgetapp.budget.BudgetNotActiveException;
import com.dave222.budgetapp.budget.BudgetNotFoundException;
import com.dave222.budgetapp.exceptions.RedundantRequestException;
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
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({
            BudgetNotActiveException.class
    })
    public ResponseEntity<String> handleNotActiveException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler({
            RedundantRequestException.class
    })
    public ResponseEntity<String> handleNoUpdateException(RuntimeException ex) {
        return ResponseEntity.noContent().build();
    }
}