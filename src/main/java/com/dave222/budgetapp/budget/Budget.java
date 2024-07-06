package com.dave222.budgetapp.budget;

import com.dave222.budgetapp.budget.enums.State;
import com.dave222.budgetapp.budget.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Budget {

    // User defined
    @NotNull(message = "name is mandatory")
    @Size(max = 255, message = "name max-length is 255 characters")
    private String name;

    @NotNull(message = "startDate is mandatory")
    private LocalDateTime startDate;

    @NotNull(message = "startBalance is mandatory")
    private BigDecimal startBalance;

    private LocalDateTime endDate;
    private BigDecimal targetBalance;
    private String description;

    // Todo: categories; create separate entity for categories and define categories as HashMap<String, category> (?)
    // Todo: accounts

    // system managed
    private @Id @GeneratedValue Long id;
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

    public Budget(String name, String description, LocalDateTime startDate, BigDecimal startBalance, LocalDateTime endDate, BigDecimal targetBalance) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.startBalance = startBalance;
        this.endDate = endDate;
        this.targetBalance = targetBalance;
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

    // Start date
    public LocalDateTime getStartDate(){
        return this.startDate;
    };

    public void setStartDate(LocalDateTime startDate){
        this.startDate = startDate;
    };

    // Start balance
    public BigDecimal getStartBalance(){
        return this.startBalance;
    };

    public void setStartBalance(BigDecimal startBalance){
        this.startBalance = startBalance;
    };

    // End date
    public LocalDateTime getEndDate(){
        return this.endDate;
    };

    public void setEndDate(LocalDateTime endDate){
        this.endDate = endDate;
    };

    // Target Balance
    public BigDecimal getTargetBalance(){
        return this.targetBalance;
    };

    public void setTargetBalance(BigDecimal targetBalance){
        this.targetBalance = targetBalance;
    };



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

    @Override
    public String toString(){
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}