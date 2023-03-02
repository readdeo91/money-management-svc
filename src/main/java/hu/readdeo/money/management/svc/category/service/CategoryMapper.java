package hu.readdeo.money.management.svc.category.service;

import hu.readdeo.money.management.svc.category.Category;
import hu.readdeo.money.management.svc.security.util.AuthenticationFacade;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    uses = {CategoryService.class},
    injectionStrategy = InjectionStrategy.FIELD)
abstract class CategoryMapper {
  @Autowired CategoryService categoryService;
  @Autowired AuthenticationFacade authenticationFacade;

  public abstract Category toCategoryDTO(CategoryPO category);

  public abstract List<Category> toCategoryDTOList(List<CategoryPO> categoryList);

  @Mapping(target = "user", ignore = true)
  public abstract CategoryPO toCategoryPO(Category categoryDTO);

  @AfterMapping
  void mapUserInCategory(CategoryPO category) {
    category.setUser(authenticationFacade.getUser());
  }
}
