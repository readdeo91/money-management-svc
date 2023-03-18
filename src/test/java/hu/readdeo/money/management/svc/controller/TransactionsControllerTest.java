package hu.readdeo.money.management.svc.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hu.readdeo.money.management.svc.securitymock.WithMockCustomUser;
import org.json.JSONObject;
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

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureMockMvc
class TransactionsControllerTest {

  @Autowired private MockMvc mvc;

  @WithMockCustomUser
  @Test
  void valid_create_EXPENSE() throws Exception {
    JSONObject requestBody = new JSONObject();
    requestBody.put("amount", -5000);
    requestBody.put("dateTime", "2021-03-19T21:16:34");
    requestBody.put("description", "desc");
    requestBody.put("sourceAccount", 1);
    requestBody.put("mainCategory", 1);
    requestBody.put("subCategory", 2);

    mvc.perform(
                    MockMvcRequestBuilders.post("/transactions")
                            .header("Content-Type", "application/json")
                            .content(requestBody.toString()))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.header().exists("Location"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
            .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(-5000))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.dateTime").value("2021-03-19T21:16:34"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("desc"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.sourceAccount").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").isString())
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.transactionsPage.href").isString());
  }

  @WithMockCustomUser
  @Test
  void create_missing_amount_response_400() throws Exception {
    JSONObject requestBody = new JSONObject();
    requestBody.put("dateTime", "2021-03-19T21:16:34");
    requestBody.put("amount", -5000);
    requestBody.put("description", "desc");
    requestBody.put("mainCategory", 1);
    requestBody.put("subCategory", 2);

    mvc.perform(
                    MockMvcRequestBuilders.post("/transactions")
                            .header("Content-Type", "application/json")
                            .content(requestBody.toString()))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @WithMockCustomUser
  @Test
  void valid_create_INCOME() throws Exception {
    JSONObject requestBody = new JSONObject();
    requestBody.put("amount", 5000);
    requestBody.put("dateTime", "2021-03-19T21:16:34");
    requestBody.put("description", "desc");
    requestBody.put("sourceAccount", 1);
    requestBody.put("type", "INCOME");
    requestBody.put("mainCategory", 1);
    requestBody.put("subCategory", 2);

    mvc.perform(
            MockMvcRequestBuilders.post("/transactions")
                .header("Content-Type", "application/json")
                .content(requestBody.toString()))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("Location"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(5000))
//        .andExpect(MockMvcResultMatchers.jsonPath("$.dateTime").value("2021-03-19T21:16:34"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("desc"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.sourceAccount").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.transactionsPage.href").isString());
  }

  @WithMockCustomUser
  @Test
  void valid_create_TRANSFER() throws Exception {
    JSONObject requestBody = new JSONObject();
    requestBody.put("amount", -5000);
    requestBody.put("dateTime", "2021-03-19T21:16:34");
    requestBody.put("description", "desc");
    requestBody.put("sourceAccount", 1);
    requestBody.put("mainCategory", 1);
    requestBody.put("subCategory", 2);

    mvc.perform(
            MockMvcRequestBuilders.post("/transactions")
                .header("Content-Type", "application/json")
                .content(requestBody.toString()))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("Location"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(-5000))
//        .andExpect(MockMvcResultMatchers.jsonPath("$.dateTime").value("2021-03-19T21:16:34"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("desc"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.sourceAccount").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.transactionsPage.href").isString());
  }

  @WithMockCustomUser
  @Test
  void valid_create_subCategory_empty() throws Exception {
    JSONObject requestBody = new JSONObject();
    requestBody.put("amount", -5000);
    requestBody.put("dateTime", "2021-03-19T21:16:34");
    requestBody.put("description", "desc");
    requestBody.put("sourceAccount", 1);
    requestBody.put("mainCategory", 1);

    mvc.perform(
            MockMvcRequestBuilders.post("/transactions")
                .header("Content-Type", "application/json")
                .content(requestBody.toString()))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("Location"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(-5000))
//        .andExpect(MockMvcResultMatchers.jsonPath("$.dateTime").value("2021-03-19T21:16:34"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("desc"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.sourceAccount").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory").doesNotExist())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.transactionsPage.href").isString());
  }

  @WithMockCustomUser
  @Test
  void create_no_mainCategory_response_400() throws Exception {
    JSONObject requestBody = new JSONObject();
    requestBody.put("amount", 5000);
    requestBody.put("dateTime", "2021-03-19T21:16:34");
    requestBody.put("description", "desc");
    requestBody.put("account", 1);

    mvc.perform(
            MockMvcRequestBuilders.post("/transactions")
                .header("Content-Type", "application/json")
                .content(requestBody.toString()))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @WithMockCustomUser
  @Test
  void page() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.get("/transactions?page=0&size=30")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.transactions[0].id").isNumber())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.transactions[0].amount").value(-5000))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.transactions[0].sourceAccount").value(1))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.transactions[0].mainCategory").value(1))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.transactions[0].subCategory").value(2))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.transactions[0]._links.self.href")
                .isString())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.transactions[0]._links.self.href")
                .isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").isString())
        .andExpect(MockMvcResultMatchers.jsonPath("$.page.size").value(30));
  }

  @WithMockCustomUser
  @Test
  void getOne() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.get("/transactions/1")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(-1000))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("sajt"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.sourceAccount").value(3))
        .andExpect(MockMvcResultMatchers.jsonPath("$.mainCategory").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory").value(2))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.self.href")
                .value("http://localhost/transactions/1"));
  }

  @WithMockCustomUser
  @Test
  void update() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.put("/transactions/1")
                .content(
                    "{\"amount\": -200, \"dateTime\": \"2021-02-17T20:17:55\",\"description\":\"update2\",\"sourceAccount\": 1,\"mainCategory\": 3,\"subCategory\": 4}")
                .header("Content-Type", "application/json"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
        .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(-200L))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("update2"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.sourceAccount")
                .value("1"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.mainCategory")
                .value("3"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.subCategory")
                .value("4"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.self.href")
                .value("http://localhost/transactions/1"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.transactionsPage.href")
                .value("http://localhost/transactions"));
  }
  //
  //  @WithMockCustomUser
  //  @Test
  //  void delete() throws Exception {
  //    Transaction newTransaction =
  //        new Transaction()
  //            .setAmount(new BigDecimal(5000))
  //            .setDescription("junit")
  //            .setMainCategory(
  //                categoriesService
  //                    .findById(UUID.fromString("30195a38-c851-438b-8724-a1225b6f0966"))
  //                    .get())
  //            .setSubCategory(
  //                categoriesService
  //                    .findById(UUID.fromString("1e101ff7-8b95-4405-bc5f-5c1d917e4dbd"))
  //                    .get())
  //            .setAccount(
  //                accountsService
  //                    .findById(UUID.fromString("99b6a126-e0d5-4c4d-a82b-264aa81f3335"))
  //                    .get())
  //            .setType(TransactionType.EXPENSE);
  //    Transaction savedTransaction = transactionsService.create(newTransaction);
  //
  //    Assert.assertNotNull(savedTransaction);
  //
  //    mvc.perform(MockMvcRequestBuilders.delete("/transactions/" + savedTransaction.getId()))
  //        .andDo(print())
  //        .andExpect(status().isNoContent());
  //
  //    Optional<Transaction> deletedTransaction = repository.findById(savedTransaction.getId());
  //    Assertions.assertFalse(deletedTransaction.isPresent());
  //  }
}
