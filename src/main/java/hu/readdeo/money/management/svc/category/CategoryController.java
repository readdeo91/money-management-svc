package hu.readdeo.money.management.svc.category;

import hu.readdeo.money.management.svc.category.service.CategoryServiceAdapter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "category")
public class CategoryController {

  private final CategoryServiceAdapter categoryService;
  private final CategoryModelAssembler categoryModelAssembler;

  @PostMapping
  public ResponseEntity<EntityModel<Category>> createCategory(@RequestBody Category category) {
    Category createdCategory = categoryService.create(category);
    EntityModel<Category> entityModel = categoryModelAssembler.toModel(createdCategory);
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<Category>>> getAllCategories() {
    List<Category> categoryList = categoryService.findAll();
    CollectionModel<EntityModel<Category>> collectionModel =
        categoryModelAssembler.toCollectionModel(categoryList);
    return new ResponseEntity<>(collectionModel, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Category>> getCategory(@PathVariable("id") Long id) {
    Category category = categoryService.findById(id);
    EntityModel<Category> entityModel = categoryModelAssembler.toModel(category);
    return new ResponseEntity<>(entityModel, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Category>> updateCategory(
      @PathVariable("id") Long id, @RequestBody Category categoryUpdate) {
    Category updatedCategory = categoryService.update(id, categoryUpdate);
    EntityModel<Category> entityModel = categoryModelAssembler.toModel(updatedCategory);
    return new ResponseEntity<>(entityModel, HttpStatus.OK);
  }

  @PatchMapping("/")
  public ResponseEntity<Void> patchCategory() {
    return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
    categoryService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
