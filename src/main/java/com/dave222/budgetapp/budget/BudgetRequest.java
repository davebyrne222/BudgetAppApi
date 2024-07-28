package com.dave222.budgetapp.budget;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@MappedSuperclass
public class BudgetRequest {

    // User defined
    @NotNull(message = "name is mandatory")
    @Size(max = 255, message = "name max-length is 255 characters")
    protected String name;

    @NotNull(message = "startDate is mandatory")
    protected Date startDate;

    @NotNull(message = "startBalance is mandatory")
    protected BigDecimal startBalance;

    protected Date endDate;
    protected BigDecimal targetBalance;
    protected String description;

    // Todo: categories; create separate entity for categories and define categories as HashMap<String, category> (?)
    // Todo: accounts

    protected BudgetRequest() {}

    public BudgetRequest(String name, String description, Date startDate, BigDecimal startBalance, Date endDate, BigDecimal targetBalance) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.startBalance = startBalance;
        this.endDate = endDate;
        this.targetBalance = targetBalance;
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

        BudgetRequest budget = (BudgetRequest) o;

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
}