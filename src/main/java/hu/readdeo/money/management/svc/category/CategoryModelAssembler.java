package hu.readdeo.money.management.svc.category;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CategoryModelAssembler
    implements RepresentationModelAssembler<Category, EntityModel<Category>> {
  @Override
  public EntityModel<Category> toModel(@NotNull Category category) {
    Link selfLink =
        linkTo(methodOn(CategoryController.class).getCategory(category.getId())).withSelfRel();
    Affordance update =
        afford(methodOn(CategoryController.class).updateCategory(category.getId(), null));
    Link aggregateRoute =
        linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories");
    return EntityModel.of(category, selfLink.andAffordance(update), aggregateRoute);
  }

  @Override
  public CollectionModel<EntityModel<Category>> toCollectionModel(
      @NotNull Iterable<? extends Category> categories) {
    List<Category> categoryList = (List<Category>) categories;
    List<EntityModel<Category>> entityModelList =
        categoryList.stream()
            .map(
                category ->
                    EntityModel.of(
                        category,
                        linkTo(methodOn(CategoryController.class).getCategory(category.getId()))
                            .withSelfRel()))
            .collect(Collectors.toList());
    return CollectionModel.of(
        entityModelList,
        linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories"));
  }
}
