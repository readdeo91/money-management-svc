package hu.readdeo.money.management.svc;

import hu.readdeo.money.management.svc.account.Account;
import hu.readdeo.money.management.svc.account.AccountService;
import hu.readdeo.money.management.svc.securitymock.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountApiTest {

  @Autowired AccountService accountService;

  @Test
  @WithMockCustomUser
  public void addAccountForUser() {
    Account account = new Account();
    account.setCurrency("HUF");
    account.setName("KH");
    account.setCredit(false);
    Account savedAccount = accountService.create(account);
    System.out.println(savedAccount);
  }
}
