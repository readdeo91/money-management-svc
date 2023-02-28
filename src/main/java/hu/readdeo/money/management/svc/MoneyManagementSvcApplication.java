package hu.readdeo.money.management.svc;

import hu.readdeo.money.management.svc.configuration.RepositoryRestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RepositoryRestConfiguration.class)
public class MoneyManagementSvcApplication {

  public static void main(String[] args) {
    SpringApplication.run(MoneyManagementSvcApplication.class, args);
  }
}
