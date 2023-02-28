package hu.readdeo.money.management.svc.configuration;

import hu.readdeo.money.management.svc.account.Account;
import hu.readdeo.money.management.svc.category.Category;
import hu.readdeo.money.management.svc.transaction.service.TransactionModel;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

public class RepositoryRestConfiguration implements RepositoryRestConfigurer {

  @Override
  public void configureRepositoryRestConfiguration(
      org.springframework.data.rest.core.config.RepositoryRestConfiguration config,
      CorsRegistry cors) {
    config.exposeIdsFor(Account.class);
    config.exposeIdsFor(Category.class);
    config.exposeIdsFor(TransactionModel.class);
  }
}
