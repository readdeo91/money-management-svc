package hu.readdeo.money.management.svc.category;

import hu.readdeo.money.management.svc.exception.ErrorResponse;
import hu.readdeo.money.management.svc.security.util.AuthenticationFacade;
import hu.readdeo.money.management.svc.transaction.service.TransactionModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryValidator {

  private final CategoryService categoryService;

  private final AuthenticationFacade authenticationFacade;

  public void validateCategories(TransactionModel transaction) {
    if (ObjectUtils.isEmpty(transaction.getMainCategory())
        && ObjectUtils.isEmpty(transaction.getSubCategory())) return;
    categoriesBelongsToUser(transaction);
    categoriesInTransactionAreValid(transaction);
  }

  private void categoriesInTransactionAreValid(TransactionModel transaction) {
    if (!isMainCategoryValid(transaction.getMainCategory())) {
      log.error(
          "mainCategory in transaction is a subCategory. User: {} transaction: {}",
          authenticationFacade.getUser().getId(),
          transaction.toString());
      throw new ErrorResponse("", "", "mainCategory error", "", HttpStatus.BAD_REQUEST);
    }
    if (!isSubCategoryValid(transaction.getSubCategory())) {
      log.error(
          "subCategory in transaction is a mainCategory. User: {} transaction: {}",
          authenticationFacade.getUser().getId(),
          transaction.toString());
      throw new ErrorResponse("", "", "subCategory error", "", HttpStatus.BAD_REQUEST);
    }
  }

  private void categoriesBelongsToUser(TransactionModel transaction) {
    if (!ObjectUtils.isEmpty(transaction.getMainCategory())
        && !categoryService.exists(transaction.getMainCategory().getId())) {
      log.error(
          "mainCategory in transaction doesn't exist for user: {} categoryId: {}",
          authenticationFacade.getUser().getId(),
          transaction.getMainCategory().getId());
      throw new ErrorResponse("", "", "mainCategory error", "", HttpStatus.BAD_REQUEST);
    }
    if (!ObjectUtils.isEmpty(transaction.getSubCategory())
        && !categoryService.exists(transaction.getSubCategory().getId())) {
      log.error(
          "subCategory in transaction doesn't exist for user: {} categoryId: {}",
          authenticationFacade.getUser().getId(),
          transaction.getSubCategory().getId());
      throw new ErrorResponse("", "", "mainCategory error", "", HttpStatus.BAD_REQUEST);
    }
  }

  private boolean isMainCategoryValid(Category category) {
    if (ObjectUtils.isEmpty(category)) return true;
    return ObjectUtils.isEmpty(category.getParentId());
  }

  private boolean isSubCategoryValid(Category category) {
    if (ObjectUtils.isEmpty(category)) return true;
    return !ObjectUtils.isEmpty(category) && !ObjectUtils.isEmpty(category.getParentId());
  }
}
