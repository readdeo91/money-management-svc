package hu.readdeo.money.management.svc.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hu.readdeo.money.management.svc.account.service.AccountPO;
import hu.readdeo.money.management.svc.account.service.AccountRepository;
import hu.readdeo.money.management.svc.account.service.AccountService;
import hu.readdeo.money.management.svc.securitymock.WithMockCustomUser;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

  @Autowired private MockMvc mvc;
  @Autowired private AccountRepository repository;
  @Autowired private AccountService accountsService;

  @WithMockCustomUser
  @Test
  void create() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .header("Content-Type", "application/json")
                .content("{\"description\": \"ERSTE\",\"currency\": \"EUR\"}"))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("Location"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
        .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value("EUR"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.accounts.href").isString());
    Assertions.assertEquals(2, repository.findById(3L).get().getUser().getId());
  }

  @WithMockCustomUser
  @Test
  void create_with_id_400() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.post("/accounts")
                .header("Content-Type", "application/json")
                .content("{\"id\":\"11\", \"description\": \"ERSTE\",\"currency\": \"EUR\"}"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    Assertions.assertEquals(2, repository.findById(3L).get().getUser().getId());
  }

  @WithMockCustomUser
  @Test
  void create_with_id_in_path_405() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.post("/accounts/11")
                .header("Content-Type", "application/json")
                .content("{\"description\": \"ERSTE\",\"currency\": \"EUR\"}"))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed());
    Assertions.assertEquals(2, repository.findById(3L).get().getUser().getId());
  }

  @WithMockCustomUser
  @Test
  void create_with_id_put_405() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.put("/accounts")
                .header("Content-Type", "application/json")
                .content("{\"id\":\"11\", \"description\": \"ERSTE\",\"currency\": \"EUR\"}"))
        .andDo(print())
        .andExpect(status().isMethodNotAllowed());
    Assertions.assertEquals(2, repository.findById(3L).get().getUser().getId());
  }

  @WithMockCustomUser
  @Test
  void create_with_id_put_400() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.put("/accounts/11")
                .header("Content-Type", "application/json")
                .content("{\"description\": \"ERSTE\",\"currency\": \"EUR\"}"))
        .andDo(print())
        .andExpect(status().isNotFound());
    Assertions.assertEquals(2, repository.findById(3L).get().getUser().getId());
  }

  @WithMockCustomUser
  @Test
  void all() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/accounts").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.accounts[0].id").value(3))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.accounts[0].description")
                .value("Raiffeisen"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.accounts[0].currency").value("HUF"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.accounts[0]._links.self.href")
                .value("http://localhost/accounts/3"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.accounts[1].id").value(4))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.accounts[1].description").value("OTP"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.accounts[1].currency").value("HUF"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.accounts[1]._links.self.href")
                .value("http://localhost/accounts/4"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.accounts.href")
                .value("http://localhost/accounts"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.accounts[2].id").value(5))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.accounts[2].description")
                .value("CreditRaiffeisen"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.accounts[2].currency").value("HUF"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.accounts[2]._links.self.href")
                .value("http://localhost/accounts/5"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.accounts.href")
                .value("http://localhost/accounts"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.accounts[3]").doesNotExist());
  }

  @WithMockCustomUser
  @Test
  void page() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.get("/accounts?page=1&size=1")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.accounts[0].id").value(4))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.accounts[0].description").value("OTP"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.accounts[0].currency").value("HUF"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.accounts[0]._links.self.href")
                .value("http://localhost/accounts/4"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.accounts[1]").doesNotExist());
  }

  @WithMockCustomUser
  @Test
  void one() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/accounts/5").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("5"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value("HUF"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.self.href")
                .value("http://localhost/accounts/5"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.accounts.href")
                .value("http://localhost/accounts"));
  }

  @WithMockCustomUser
  @Test
  void one_other_users_account_cant_be_accessed() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/accounts/1").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @WithMockCustomUser
  @Test
  void update() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.put("/accounts/5")
                .content("{\"description\": \"KH\",\"currency\": \"EUR\"}")
                .header("Content-Type", "application/json"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(5))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("KH"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value("EUR"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.self.href")
                .value("http://localhost/accounts/5"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.accounts.href")
                .value("http://localhost/accounts"));
  }

  @WithMockCustomUser
  @Test
  void update_other_users_account_not_updated() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.put("/accounts/1")
                .content("{\"description\": \"KH\",\"currency\": \"EUR\"}")
                .header("Content-Type", "application/json"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @WithMockCustomUser
  @Test
  void delete() throws Exception {
    AccountPO newAccount = new AccountPO().setCurrency("USD").setName("Usánka");
    AccountPO savedAccount = accountsService.create(newAccount);

    Assertions.assertNotNull(savedAccount);

    mvc.perform(MockMvcRequestBuilders.delete("/accounts/" + savedAccount.getId()))
        .andDo(print())
        .andExpect(status().isNoContent());

    Optional<AccountPO> deletedAccount = repository.findById(savedAccount.getId());
    Assertions.assertFalse(deletedAccount.isPresent());
  }

  @WithMockCustomUser
  @Test
  void delete_other_users_account_no_content_account_not_deleted() throws Exception {

    mvc.perform(MockMvcRequestBuilders.delete("/accounts/1"))
        .andDo(print())
        .andExpect(status().isNoContent());

    Optional<AccountPO> deletedAccount = repository.findById(1L);
    Assertions.assertTrue(deletedAccount.isPresent());
  }
}
