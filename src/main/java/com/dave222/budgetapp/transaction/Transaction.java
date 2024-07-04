package com.dave222.budgetapp.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    private @Id @GeneratedValue Long id;
    private Long budgetId;
    private Long categoryId;
    private Long accountId;
    private LocalDateTime transactionDate;
    private String description; // how to limit number of chars?
    private BigDecimal amount;
    private String note;
    private LocalDateTime created;
    private LocalDateTime updated;

    protected Transaction() {
    }

    public Transaction(Long budgetId,
                       Long categoryId,
                       Long accountId,
                       LocalDateTime transactionDate,
                       String description,
                       BigDecimal amount,
                       String note) {
        this.budgetId = budgetId;
        this.categoryId = categoryId;
        this.accountId = accountId;
        this.transactionDate = transactionDate;
        this.description = description;
        this.amount = amount;
        this.note = note;
    }

    // Set created time on jpa initial save
    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
    }

    // Set updated time on jpa save
    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }

    // Id
    public Long getId(){ return this.id; }

    public void setId(Long id){ this.id = id; }

    // Budget Id
    public Long getBudgetId(){ return this.budgetId; }

    public void setBudgetId(Long budgetId){ this.budgetId = budgetId; }

    // Category Id
    public Long getCategoryId(){ return this.categoryId; }

    public void setCategoryId(Long categoryId){ this.categoryId = categoryId; }

    // Account Id
    public Long getAccountId(){ return this.accountId; }

    public void setAccountId(Long accountId){ this.accountId = accountId; }

    // Transaction Id
    public LocalDateTime getTransactionDate(){ return this.transactionDate; }

    public void setTransactionDate(LocalDateTime transactionDate){ this.transactionDate = transactionDate; }

    // Description
    public String getDescription(){ return this.description; }

    public void setDescription(String description){ this.description = description; }

    // Amount
    public BigDecimal getAmount(){ return this.amount; }

    public void setAmount(BigDecimal amount){ this.amount = amount; }

    // Note
    public String getNote(){ return this.note; }

    public void setNote(String Note){ this.note = Note; }

    // Created
    public LocalDateTime getCreated(){ return this.created; }

    public void setCreated(LocalDateTime created){ this.created = created; }

    // Updated
    public LocalDateTime getUpdated(){ return this.updated; }

    public void setUpdated(LocalDateTime updated){ this.updated = updated; }
}