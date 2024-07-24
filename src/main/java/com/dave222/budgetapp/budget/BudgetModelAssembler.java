package com.dave222.budgetapp.budget;

import com.dave222.budgetapp.budget.enums.State;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class BudgetModelAssembler implements RepresentationModelAssembler<Budget, EntityModel<Budget>> {

    @Override
    public EntityModel<Budget> toModel(Budget budget) {

        EntityModel<Budget> budgetModel = EntityModel.of(budget,
                linkTo(methodOn(BudgetController.class).getOne(budget.getId())).withSelfRel(),
                linkTo(methodOn(BudgetController.class).all()).withRel("all"),
                linkTo(methodOn(BudgetController.class).delete(budget.getId())).withRel("delete"));

        if (budget.getState() != State.ACTIVE){
            budgetModel.add(linkTo(methodOn(BudgetController.class).archive(budget.getId())).withRel("archive"));
        } else {
            budgetModel.add(linkTo(methodOn(BudgetController.class).dearchive(budget.getId())).withRel("de-archive"));
        }

        return budgetModel;
    }
}