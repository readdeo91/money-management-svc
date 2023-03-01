package hu.readdeo.money.management.svc.account.service;

import hu.readdeo.money.management.svc.account.Account;
import hu.readdeo.money.management.svc.security.util.AuthenticationFacade;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
abstract class AccountMapper {

  @Autowired AuthenticationFacade authenticationFacade;

  public abstract Account toAccountDTO(AccountPO transaction);

  public abstract List<Account> toAccountDTOList(List<AccountPO> transactionList);

  @Mapping(target = "user", ignore = true)
  public abstract AccountPO toAccountModel(Account transactionDTO);

  @AfterMapping
  void mapUserInAccount(AccountPO transaction) {
    transaction.setUser(authenticationFacade.getUser());
  }
}
