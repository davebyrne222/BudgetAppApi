package com.dave222.budgetapp.budget;

import com.dave222.budgetapp.budget.enums.State;
import com.dave222.budgetapp.budget.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Budget {

    private @Id @GeneratedValue Long id;
    private String name;
    private String description;
    private Status status = Status.BALANCED;
    private State state = State.ACTIVE;
    private LocalDateTime created;
    private LocalDateTime updated;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal totalOutgoing = BigDecimal.ZERO;

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

    protected Budget() {}

    public Budget(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // ID
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Name
    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    // Description
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Status
    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // State
    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    // Created
    public LocalDateTime getCreated() { return this.created; }

    public void setCreated(LocalDateTime created) { this.created = created; }

    // Updated
    public LocalDateTime getUpdated() { return this.updated; }

    public void setUpdated(LocalDateTime updated) { this.updated = updated; }

    // Total amount
    public BigDecimal getTotalAmount() { return this.totalAmount; }

    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    // Total Income
    public BigDecimal getTotalIncome() { return this.totalIncome; }

    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }

    // Total Outgoing
    public BigDecimal getTotalOutgoing() { return this.totalOutgoing; }

    public void setTotalOutgoing(BigDecimal totalOutgoing) { this.totalOutgoing = totalOutgoing; }

    // Utils
    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (!(o instanceof Budget))
            return false;

        Budget budget = (Budget) o;

        return Objects.equals(this.id, budget.id)
                && Objects.equals(this.description, budget.description)
                && this.status == budget.status;
    }
}