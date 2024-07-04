package com.dave222.budgetapp.budget;

import com.dave222.budgetapp.budget.enums.State;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class BudgetController {

    private final BudgetRepository budgetRepository;
    private final BudgetModelAssembler budgetModelAssembler;

    BudgetController(BudgetRepository budgetRepository, BudgetModelAssembler budgetModelAssembler) {

        this.budgetRepository = budgetRepository;
        this.budgetModelAssembler = budgetModelAssembler;
    }

    // Create
    @PostMapping("/budgets")
    ResponseEntity<EntityModel<Budget>> newBudget(@RequestBody Budget budget) {

        Budget newBudget = budgetRepository.save(budget);

        return ResponseEntity
                .created(linkTo(methodOn(BudgetController.class).one(newBudget.getId())).toUri())
                .body(budgetModelAssembler.toModel(newBudget));
    }

    // Read
    @GetMapping("/budgets")
    CollectionModel<EntityModel<Budget>> all() {

        List<EntityModel<Budget>> budgets = budgetRepository.findAll().stream()
                .map(budgetModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(budgets,
                linkTo(methodOn(BudgetController.class).all()).withSelfRel());
    }

    @GetMapping("/budgets/{id}")
    EntityModel<Budget> one(@PathVariable Long id) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        return budgetModelAssembler.toModel(budget);
    }

    // Update
    @PutMapping("/budgets/{id}")
    ResponseEntity<?> update(@RequestBody Budget newBudget, @PathVariable Long id) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        // Cannot update archived budget
        if (budget.getState() != State.ACTIVE){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Method Forbidden")
                            .withDetail("Budget of state " + budget.getState() + " can not be updated"));
        }

        // No difference between stored and new budgets
        if (budget.equals(newBudget)) {
            return ResponseEntity.noContent().build();
        }

        // Replace old budget with new:
        newBudget.setId(budget.getId());

        return ResponseEntity.ok(budgetModelAssembler.toModel(budgetRepository.save(newBudget)));

    }

    @PutMapping("/budgets/{id}/archive")
    ResponseEntity<?> archive(@PathVariable Long id) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        if (budget.getState() == State.ACTIVE) {
            budget.setState(State.ARCHIVED);
            budgetRepository.save(budget);
            return ResponseEntity.ok(budgetModelAssembler.toModel(budget));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method Not Allowed")
                        .withDetail("You can't archive a budget that is in the " + budget.getState() + " state"));
    }

    @PutMapping("/budgets/{id}/dearchive")
    ResponseEntity<?> dearchive(@PathVariable Long id) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        if (budget.getState() == State.ARCHIVED) {
            budget.setState(State.ACTIVE);
            budgetRepository.save(budget);
            return ResponseEntity.ok(budgetModelAssembler.toModel(budget));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method Not Allowed")
                        .withDetail("You can't de-archive a budget that is in the " + budget.getState() + " state"));
    }

    // Delete
    @DeleteMapping("/budgets/{id}/delete")
    ResponseEntity<?> delete(@PathVariable Long id) {

       if (!budgetRepository.existsById(id)) {
           throw new BudgetNotFoundException(id);
       }

       budgetRepository.deleteById(id);
        /*
        TODO: Delete transactions
        https://howtodoinjava.com/hibernate/hibernate-jpa-cascade-types/
        transactionRepository.deleteById(id);
        */

        return ResponseEntity.ok("Budget deleted");
    }}