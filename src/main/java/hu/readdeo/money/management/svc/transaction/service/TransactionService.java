package hu.readdeo.money.management.svc.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import hu.readdeo.money.management.svc.category.CategoryValidator;
import hu.readdeo.money.management.svc.exception.ErrorResponse;
import hu.readdeo.money.management.svc.security.model.User;
import hu.readdeo.money.management.svc.security.util.AuthenticationFacade;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionsRepository transactionsRepository;
  private final CategoryValidator categoryValidator;
  private final AuthenticationFacade auth;

  @Transactional
  public TransactionModel create(TransactionModel transaction) {
    validateCreation(transaction);
    setUserAndAddId(transaction);
    return saveOrErrorResponse(transaction);
  }

  public TransactionModel findById(UUID id) {
    TransactionModel transaction =
        transactionsRepository.findFirstByUserAndId(auth.getUser(), id).orElse(null);
    throwIfNotFound(transaction);
    return transaction;
  }

  public Page<TransactionModel> getPage(Pageable pageable) {
    Page<TransactionModel> transactionPage =
        transactionsRepository.findByUserOrderByDateTimeDesc(auth.getUser(), pageable);
    throwIfNotFound(transactionPage);
    return transactionPage;
  }

  public TransactionModel patch(UUID id, JsonPatch patch) {
    throwIfDoesntExist(id);
    TransactionModel patchedTransaction = applyPatch(patch, id);
    return update(id, patchedTransaction);
  }

  public TransactionModel update(UUID id, TransactionModel transactionUpdate) {
    throwIfDoesntExist(id);
    setIdAndUser(id, transactionUpdate);
    categoryValidator.validateCategories(transactionUpdate);
    try {
      return transactionsRepository.save(transactionUpdate);
    } catch (Exception e) {
      throw updateErrorResponse(transactionUpdate, e);
    }
  }

  @Transactional
  public void delete(UUID id) {
    User user = auth.getUser();
    throwIfDoesntExist(id);
    transactionsRepository.deleteByUserAndId(user, id);
  }

  private void setIdAndUser(UUID id, TransactionModel transactionUpdate) {
    transactionUpdate.setId(id);
    transactionUpdate.setUser(auth.getUser());
  }

  private void setUserAndAddId(TransactionModel transaction) {
    transaction.setUser(auth.getUser());
    transaction.setId(UUID.randomUUID());
  }

  private void throwIfContainsId(TransactionModel transaction) {
    if (!ObjectUtils.isEmpty(transaction.getId())) {
      throw new ErrorResponse("", "", "New entry cannot contain ID", "", HttpStatus.BAD_REQUEST);
    }
  }

  private void throwIfNotFound(TransactionModel transactionObject) {
    if (ObjectUtils.isEmpty(transactionObject)) {
      throw new ErrorResponse("Transaction not found", HttpStatus.NOT_FOUND);
    }
  }

  private void throwIfNotFound(Page<TransactionModel> transactionObject) {
    if (ObjectUtils.isEmpty(transactionObject)) {
      throw new ErrorResponse("Transaction not found", HttpStatus.NOT_FOUND);
    }
  }

  private void throwIfDoesntExist(UUID id) {
    if (!transactionsRepository.existsByUserAndId(auth.getUser(), id)) {
      throw new ErrorResponse("Transaction not found", HttpStatus.NOT_FOUND);
    }
  }

  private TransactionModel applyPatch(JsonPatch patch, UUID id) {
    TransactionModel targetTransaction = findById(id);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode patched = patch.apply(objectMapper.convertValue(targetTransaction, JsonNode.class));
      return objectMapper.treeToValue(patched, TransactionModel.class);
    } catch (JsonPatchException | JsonProcessingException e) {
      UUID errorId = UUID.randomUUID();
      log.error("Failed to apply patch. errorId: {} Exception: {}", errorId, e);
      throw new ErrorResponse("Failed to apply patch", errorId, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private ErrorResponse createErrorResponse(TransactionModel transaction, Exception e) {
    UUID errorId = UUID.randomUUID();
    log.error(
        "Could not save new transaction: {} Exception: {}, errorId: {}", transaction, e, errorId);
    return new ErrorResponse(
        "", errorId.toString(), "Could not create transaction", "", HttpStatus.SERVICE_UNAVAILABLE);
  }

  private ErrorResponse updateErrorResponse(TransactionModel transactionUpdate, Exception e) {
    UUID errorId = UUID.randomUUID();
    log.error(
        "Could not update transaction: {} Error: {} ErrorId: {}", transactionUpdate, e, errorId);
    return new ErrorResponse("", errorId.toString(), "", "", HttpStatus.SERVICE_UNAVAILABLE);
  }

  private void throwIfTransferInvalid(TransactionModel transaction) {
    if (isTransferAndAmountIsPositive(transaction)) {
      throw new ErrorResponse(
          "Amount cannot be positive for a transfer type transaction", HttpStatus.BAD_REQUEST);
    }
  }

  private boolean isTransferAndAmountIsPositive(TransactionModel transaction) {
    return !ObjectUtils.isEmpty(transaction.getDestinationAccount())
        && bigDecimalIsPositive(transaction.getAmount());
  }

  private boolean bigDecimalIsPositive(BigDecimal bigDecimal) {
    return bigDecimal.compareTo(BigDecimal.ZERO) > 0;
  }

  private TransactionModel saveOrErrorResponse(TransactionModel transaction) {
    try {
      return transactionsRepository.save(transaction);
    } catch (Exception e) {
      throw createErrorResponse(transaction, e);
    }
  }

  private void validateCreation(TransactionModel transaction) {
    throwIfContainsId(transaction);
    throwIfTransferInvalid(transaction);
    categoryValidator.validateCategories(transaction);
  }
}
