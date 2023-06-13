package hu.readdeo.money.management.svc.account;

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
public class AccountModelAssembler
    implements RepresentationModelAssembler<Account, EntityModel<Account>> {
  @Override
  public EntityModel<Account> toModel(@NotNull Account account) {
    Link selfLink =
        linkTo(methodOn(AccountController.class).getAccount(account.getId())).withSelfRel();
    Affordance update =
        afford(methodOn(AccountController.class).updateAccount(account.getId(), null));
    Link aggregateRoute =
        linkTo(methodOn(AccountController.class).getAllAccounts()).withRel("accounts");
    return EntityModel.of(account, selfLink.andAffordance(update), aggregateRoute);
  }

  @Override
  public CollectionModel<EntityModel<Account>> toCollectionModel(
      @NotNull Iterable<? extends Account> accounts) {
    List<Account> accountList = (List<Account>) accounts;
    List<EntityModel<Account>> entityModelList =
        accountList.stream()
            .map(
                account ->
                    EntityModel.of(
                        account,
                        linkTo(methodOn(AccountController.class).getAccount(account.getId()))
                            .withSelfRel()))
            .collect(Collectors.toList());
    return CollectionModel.of(
        entityModelList,
        linkTo(methodOn(AccountController.class).getAccountPage(Pageable.ofSize(20)))
            .withRel("accountsPage"),
        linkTo(methodOn(AccountController.class).getAllAccounts()).withRel("accounts"));
  }
}
