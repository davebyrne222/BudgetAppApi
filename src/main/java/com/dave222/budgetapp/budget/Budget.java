package com.dave222.budgetapp.budget;

import com.dave222.budgetapp.budget.enums.State;
import com.dave222.budgetapp.budget.enums.Status;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Budget {

    private @Id @GeneratedValue Long id;
    private String name;
    private String description;
    private Status status = Status.BALANCED;;
    private State state = State.ACTIVE;

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