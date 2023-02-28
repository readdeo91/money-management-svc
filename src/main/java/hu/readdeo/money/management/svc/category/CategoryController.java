package hu.readdeo.money.management.svc.category;

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
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;
  private final CategoryModelAssembler categoryModelAssembler;

  @PostMapping
  public ResponseEntity<EntityModel<Category>> create(@RequestBody Category category) {
    EntityModel<Category> entityModel =
        categoryModelAssembler.toModel(categoryService.create(category));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<Category>>> all() {
    List<Category> categoryList = categoryService.findAll();
    return new ResponseEntity<>(
        categoryModelAssembler.toCollectionModel(categoryList), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Category>> one(@PathVariable("id") Long id) {
    return new ResponseEntity<>(
        categoryModelAssembler.toModel(categoryService.findById(id)), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Category>> update(
      @PathVariable("id") Long id, @RequestBody Category updatedCategory) {
    return new ResponseEntity<>(
        categoryModelAssembler.toModel(categoryService.update(id, updatedCategory)), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    categoryService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
