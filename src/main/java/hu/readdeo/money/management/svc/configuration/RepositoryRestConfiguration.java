package hu.readdeo.money.management.svc.configuration;

import hu.readdeo.money.management.svc.account.service.AccountPO;
import hu.readdeo.money.management.svc.category.service.CategoryPO;
import hu.readdeo.money.management.svc.transaction.service.TransactionPO;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

public class RepositoryRestConfiguration implements RepositoryRestConfigurer {

  @Override
  public void configureRepositoryRestConfiguration(
      org.springframework.data.rest.core.config.RepositoryRestConfiguration config,
      CorsRegistry cors) {
    config.exposeIdsFor(AccountPO.class);
    config.exposeIdsFor(CategoryPO.class);
    config.exposeIdsFor(TransactionPO.class);
  }
}
