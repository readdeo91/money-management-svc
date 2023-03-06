package hu.readdeo.money.management.svc.category.service;

import hu.readdeo.money.management.svc.exception.ErrorResponse;
import hu.readdeo.money.management.svc.security.model.User;
import hu.readdeo.money.management.svc.security.util.AuthenticationFacade;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final AuthenticationFacade authenticationFacade;

  public CategoryPO create(CategoryPO category) {
    throwIfContainsId(category);
    category.setUser(authenticationFacade.getUser());
    try {
      return categoryRepository.save(category);
    } catch (Exception e) {
      throw createErrorResponse(category, e);
    }
  }

  public CategoryPO findById(Long id) {
    User user = authenticationFacade.getUser();
    CategoryPO category = categoryRepository.findByUserAndId(user, id).orElse(null);
    throwIfNotFound(category);
    return category;
  }

  public List<CategoryPO> findAll() {
    User user = authenticationFacade.getUser();
    List<CategoryPO> categoryList = categoryRepository.findByUser(user);
    throwIfNotFound(categoryList);
    return categoryList;
  }

  public CategoryPO update(Long id, CategoryPO categoryUpdate) {
    throwIfDoesntExist(id);
    setIdAndUser(id, categoryUpdate);
    try {
      return categoryRepository.save(categoryUpdate);
    } catch (Exception e) {
      throw updateErrorResponse(categoryUpdate, e);
    }
  }

  @Transactional
  public void delete(Long id) {
    User user = authenticationFacade.getUser();
    categoryRepository.deleteByUserAndId(user, id);
  }

  private void setIdAndUser(Long id, CategoryPO updatedCategory) {
    updatedCategory.setId(id);
    updatedCategory.setUser(authenticationFacade.getUser());
  }

  public boolean exists(Long id) {
    User user = authenticationFacade.getUser();
    return categoryRepository.existsByUserAndId(user, id);
  }

  private void throwIfNotFound(CategoryPO categoryObject) {
    if (ObjectUtils.isEmpty(categoryObject)) {
      throw new ErrorResponse("Category not found", HttpStatus.NOT_FOUND);
    }
  }

  private void throwIfNotFound(List<CategoryPO> categoryList) {
    if (ObjectUtils.isEmpty(categoryList)) {
      throw new ErrorResponse("Category not found", HttpStatus.NOT_FOUND);
    }
  }

  private void throwIfDoesntExist(Long id) {
    User user = authenticationFacade.getUser();
    if (!categoryRepository.existsByUserAndId(user, id)) {
      throw new ErrorResponse("Category not found", HttpStatus.NOT_FOUND);
    }
  }

  private void throwIfContainsId(CategoryPO category) {
    if (!ObjectUtils.isEmpty(category.getId())) {
      throw new ErrorResponse("", "", "New entry cannot contain ID", "", HttpStatus.BAD_REQUEST);
    }
  }

  private ErrorResponse createErrorResponse(CategoryPO category, Exception e) {
    UUID errorId = UUID.randomUUID();
    log.error(
        "Could not save new category: {} error: {}, errorId: {}",
        category,
        e.getMessage(),
        errorId);
    return new ErrorResponse(
        null,
        errorId.toString(),
        "Could not create new category",
        null,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse updateErrorResponse(CategoryPO categoryUpdate, Exception e) {
    UUID errorId = UUID.randomUUID();
    log.error(
        "Could not update category: {} Error: {} ErrorId: {}",
        categoryUpdate,
        e.getMessage(),
        errorId);
    throw new ErrorResponse(
        null,
        errorId.toString(),
        "Could not update category",
        null,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
