package hu.readdeo.money.management.svc.transaction;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TransactionModelAssembler
    implements RepresentationModelAssembler<Transaction, EntityModel<Transaction>> {
  @Override
  public EntityModel<Transaction> toModel(@NotNull Transaction transaction) {
    Link selfLink =
        linkTo(methodOn(TransactionController.class).one(transaction.getId())).withSelfRel();
    Affordance update =
        afford(methodOn(TransactionController.class).update(transaction.getId(), null));
    Link aggregateRoute =
        linkTo(methodOn(TransactionController.class).page(Pageable.ofSize(20)))
            .withRel("transactionsPage");
    return EntityModel.of(transaction, selfLink.andAffordance(update), aggregateRoute);
  }

  @Override
  public CollectionModel<EntityModel<Transaction>> toCollectionModel(
      @NotNull Iterable<? extends Transaction> transactions) {
    List<Transaction> transactionList = (List<Transaction>) transactions;
    List<EntityModel<Transaction>> entityModelList =
        transactionList.stream()
            .map(
                transaction ->
                    EntityModel.of(
                        transaction,
                        linkTo(methodOn(TransactionController.class).one(transaction.getId()))
                            .withSelfRel()))
            .collect(Collectors.toList());
    return CollectionModel.of(
        entityModelList,
        linkTo(methodOn(TransactionController.class).page(Pageable.ofSize(20)))
            .withRel("transactionsPage"));
  }
}
