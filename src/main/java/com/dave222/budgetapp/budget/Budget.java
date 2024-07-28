package com.dave222.budgetapp.budget;

import com.dave222.budgetapp.budget.enums.State;
import com.dave222.budgetapp.budget.enums.Status;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Budget extends BudgetRequest {

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

    public Budget(String name, String description, Date startDate, BigDecimal startBalance, Date endDate, BigDecimal targetBalance) {
        super(
                name,
                description,
                startDate,
                startBalance,
                endDate,
                targetBalance
        );
    }

    public Budget(BudgetRequest request) {
         this(
                request.getName(),
                request.getDescription(),
                request.getStartDate(),
                request.getStartBalance(),
                request.getEndDate(),
                request.getTargetBalance()
        );
    }

    public BudgetRequest toRequest() {
        return new BudgetRequest(
                getName(),
                getDescription(),
                getStartDate(),
                getStartBalance(),
                getEndDate(),
                getTargetBalance()
        );
    }

    public void updateFromRequest(BudgetRequest request) {
        setName(request.getName());
        setDescription(request.getDescription());
        setStartDate(request.getStartDate());
        setStartBalance(request.getStartBalance());
        setEndDate(request.getEndDate());
        setTargetBalance(request.getTargetBalance());
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
    public Date getStartDate(){
        return this.startDate;
    };

    public void setStartDate(Date startDate){
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
    public Date getEndDate(){
        return this.endDate;
    };

    public void setEndDate(Date endDate){
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Budget budget = (Budget) o;

        // Use reflection to compare all fields
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object thisValue = field.get(this);
                Object otherValue = field.get(budget);

                if (!Objects.equals(thisValue, otherValue)) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field values", e);
            }
        }
        return true;
    }


    @Override
    public String toString(){
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}