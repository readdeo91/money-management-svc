package hu.readdeo.money.management.svc.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hu.readdeo.money.management.svc.category.service.CategoryPO;
import hu.readdeo.money.management.svc.category.service.CategoryRepository;
import hu.readdeo.money.management.svc.category.service.CategoryService;
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
class CategoryControllerTest {

  @Autowired private MockMvc mvc;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private CategoryService categoryService;

  @WithMockCustomUser
  @Test
  void create() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.post("/category")
                .header("Content-Type", "application/json")
                .content("{\"name\": \"testAdd\",\"parentId\": \"1\"}"))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testAdd"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.parentId").value("1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.color").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").isString())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.category.href")
                .value("http://localhost/category"));
  }

  @WithMockCustomUser
  @Test
  void all() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/category").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.categoryList[0].id").value("1"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.categoryList[0].name").value("TestParent"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.categoryList[0]._links.self.href")
                .value("http://localhost/category/1"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._embedded.categoryList[1]._links.self.href")
                .value("http://localhost/category/1"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.category.href")
                .value("http://localhost/category"));
  }

  @WithMockCustomUser
  @Test
  void one() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/category/1").accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestParent"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.parentId").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.color").isEmpty())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.self.href")
                .value("http://localhost/category/1"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.category.href")
                .value("http://localhost/category"));
  }

  @WithMockCustomUser
  @Test
  void update() throws Exception {
    mvc.perform(
            MockMvcRequestBuilders.put("/category/1")
                .content("{\"name\": \"testadd\",\"parent\": null}")
                .header("Content-Type", "application/json"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testadd"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.parentId").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.color").isEmpty())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.self.href")
                .value("http://localhost/category/1"))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$._links.category.href")
                .value("http://localhost/category"));
  }

  @WithMockCustomUser
  @Test
  void delete() throws Exception {
    CategoryPO newCategory = new CategoryPO().setName("newCat").setParentId(1L);
    CategoryPO savedCategory = categoryService.create(newCategory);

    Assertions.assertNotNull(savedCategory);

    mvc.perform(MockMvcRequestBuilders.delete("/category/" + savedCategory.getId()))
        .andDo(print())
        .andExpect(status().isNoContent());

    Optional<CategoryPO> deletedTransaction = categoryRepository.findById(savedCategory.getId());
    Assertions.assertFalse(deletedTransaction.isPresent());
  }
}
