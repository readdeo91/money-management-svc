package hu.readdeo.money.management.svc.category.service;

import hu.readdeo.money.management.svc.category.Category;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryServiceAdapter {

  private final CategoryService service;
  private final CategoryMapper mapper;

  public Category create(Category category) {
    CategoryPO categoryToCreate = mapper.toCategoryPO(category);
    CategoryPO createdCategory = service.create(categoryToCreate);
    return mapper.toCategoryDTO(createdCategory);
  }

  public Category findById(Long id) {
    CategoryPO foundCategory = service.findById(id);
    return mapper.toCategoryDTO(foundCategory);
  }

  public List<Category> findAll() {
    List<CategoryPO> categoryList = service.findAll();
    return mapper.toCategoryDTOList(categoryList);
  }

  public Category update(Long id, Category categoryUpdate) {
    CategoryPO categoryUpdatePO = mapper.toCategoryPO(categoryUpdate);
    CategoryPO updatedCategory = service.update(id, categoryUpdatePO);
    return mapper.toCategoryDTO(updatedCategory);
  }

  @Transactional
  public void delete(Long id) {
    service.delete(id);
  }

  public boolean exists(Long id) {
    return service.exists(id);
  }
}
