package com.dave222.budgetapp.transaction;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class TransactionModelAssembler implements RepresentationModelAssembler<Transaction, EntityModel<Transaction>> {

    @Override
    public EntityModel<Transaction> toModel(Transaction transaction) {

        return EntityModel.of(transaction,
                linkTo(methodOn(TransactionController.class).one(transaction.getId())).withSelfRel(),
                linkTo(methodOn(TransactionController.class).delete(transaction.getId())).withRel("delete"));

    }
}