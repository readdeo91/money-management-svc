package hu.readdeo.money.management.svc.transaction.service;

import hu.readdeo.money.management.svc.account.service.AccountPO;
import hu.readdeo.money.management.svc.account.service.AccountService;
import hu.readdeo.money.management.svc.category.service.CategoryPO;
import hu.readdeo.money.management.svc.category.service.CategoryService;
import hu.readdeo.money.management.svc.security.util.AuthenticationFacade;
import hu.readdeo.money.management.svc.transaction.Transaction;
import java.util.List;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

@Mapper(
    componentModel = "spring",
    uses = {AccountService.class},
    injectionStrategy = InjectionStrategy.FIELD)
abstract class TransactionMapper {

  @Autowired AccountService accountService;
  @Autowired CategoryService categoryService;
  @Autowired AuthenticationFacade authenticationFacade;

  @Named("accountToId")
  Long accountToId(AccountPO account) {
    if (ObjectUtils.isEmpty(account)) return null;
    return account.getId();
  }

  @Named("accountFromId")
  AccountPO accountFromId(Long id) {
    if (ObjectUtils.isEmpty(id)) return null;
    return accountService.findById(id);
  }

  @Named("mainCategoryToId")
  Long mainCategoryToId(CategoryPO mainCategory) {
    if (ObjectUtils.isEmpty(mainCategory)) return null;
    return mainCategory.getId();
  }

  @Named("subCategoryToId")
  Long subCategoryToId(CategoryPO subCategory) {
    if (ObjectUtils.isEmpty(subCategory)) return null;
    return subCategory.getId();
  }

  @Named("mainCategoryFromId")
  CategoryPO mainCategoryFromId(Long id) {
    if (ObjectUtils.isEmpty(id)) return null;
    return categoryService.findById(id);
  }

  @Named("subCategoryFromId")
  CategoryPO subCategoryFromId(Long id) {
    if (ObjectUtils.isEmpty(id)) return null;
    return categoryService.findById(id);
  }

  @Mapping(source = "sourceAccount", target = "sourceAccount", qualifiedByName = "accountToId")
  @Mapping(
      source = "destinationAccount",
      target = "destinationAccount",
      qualifiedByName = "accountToId")
  @Mapping(source = "mainCategory", target = "mainCategory", qualifiedByName = "mainCategoryToId")
  @Mapping(source = "subCategory", target = "subCategory", qualifiedByName = "subCategoryToId")
  public abstract Transaction toTransactionDTO(TransactionPO transaction);

  public abstract List<Transaction> toTransactionDTOList(List<TransactionPO> transactionList);

  @Mapping(target = "user", ignore = true)
  @Mapping(source = "sourceAccount", target = "sourceAccount", qualifiedByName = "accountFromId")
  @Mapping(
      source = "destinationAccount",
      target = "destinationAccount",
      qualifiedByName = "accountFromId")
  @Mapping(source = "mainCategory", target = "mainCategory", qualifiedByName = "mainCategoryFromId")
  @Mapping(source = "subCategory", target = "subCategory", qualifiedByName = "subCategoryFromId")
  public abstract TransactionPO toTransactionModel(Transaction transactionDTO);

  @AfterMapping
  void mapUserInTransaction(TransactionPO transaction) {
    transaction.setUser(authenticationFacade.getUser());
  }
}
