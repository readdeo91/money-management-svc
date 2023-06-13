package hu.readdeo.money.management.svc.transaction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import hu.readdeo.money.management.svc.account.service.AccountPO;
import hu.readdeo.money.management.svc.category.service.CategoryValidator;
import hu.readdeo.money.management.svc.exception.ErrorResponse;
import hu.readdeo.money.management.svc.security.model.User;
import hu.readdeo.money.management.svc.security.util.AuthenticationFacade;
import hu.readdeo.money.management.svc.transaction.id.TransactionCompositeIdHandler;
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
  private final TransactionCompositeIdHandler idHandler;
  private final CategoryValidator categoryValidator;
  private final AuthenticationFacade auth;

  public long accountBalance(AccountPO account) {
    try {
      return transactionsRepository.accountBalance(account);
    } catch (Exception e) {
      log.error("Failed to get balance for account: {}, error: {}", account, e.toString());
    }
    return 0;
  }

  @Transactional
  public TransactionPO create(TransactionPO transaction) {
    validateCreation(transaction);
    setUserAndId(transaction);
    return saveOrErrorResponse(transaction);
  }

  public TransactionPO findById(Long id) {
    TransactionPO transaction =
        transactionsRepository.findFirstByUserAndId(auth.getUser(), id).orElse(null);
    throwIfNotFound(transaction);
    return transaction;
  }

  public Page<TransactionPO> getPage(Pageable pageable) {
    Page<TransactionPO> transactionPage =
        transactionsRepository.findByUserOrderByDateTimeAsc(auth.getUser(), pageable);
    throwIfNotFound(transactionPage);
    return transactionPage;
  }

  public TransactionPO patch(Long id, JsonPatch patch) {
    throwIfDoesntExist(id);
    TransactionPO patchedTransaction = applyPatch(patch, id);
    return update(id, patchedTransaction);
  }

  public TransactionPO update(Long id, TransactionPO transactionUpdate) {
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
  public void delete(Long id) {
    User user = auth.getUser();
    throwIfDoesntExist(id);
    transactionsRepository.deleteByUserAndId(user, id);
  }

  private void setIdAndUser(Long id, TransactionPO transactionUpdate) {
    transactionUpdate.setId(id);
    transactionUpdate.setUser(auth.getUser());
  }

  private void setUserAndId(TransactionPO transaction) {
    transaction.setUser(auth.getUser());
    idHandler.setIdForNewTransaction(transaction);
  }

  private void throwIfContainsId(TransactionPO transaction) {
    if (!ObjectUtils.isEmpty(transaction.getId())) {
      throw new ErrorResponse("", "", "New entry cannot contain ID", "", HttpStatus.BAD_REQUEST);
    }
  }

  private void throwIfNotFound(TransactionPO transactionObject) {
    if (ObjectUtils.isEmpty(transactionObject)) {
      throw new ErrorResponse("Transaction not found", HttpStatus.NOT_FOUND);
    }
  }

  private void throwIfNotFound(Page<TransactionPO> transactionObject) {
    if (ObjectUtils.isEmpty(transactionObject)) {
      throw new ErrorResponse("Transaction not found", HttpStatus.NOT_FOUND);
    }
  }

  private void throwIfDoesntExist(Long id) {
    if (!transactionsRepository.existsByUserAndId(auth.getUser(), id)) {
      throw new ErrorResponse("Transaction not found", HttpStatus.NOT_FOUND);
    }
  }

  private TransactionPO applyPatch(JsonPatch patch, Long id) {
    TransactionPO targetTransaction = findById(id);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode patched = patch.apply(objectMapper.convertValue(targetTransaction, JsonNode.class));
      return objectMapper.treeToValue(patched, TransactionPO.class);
    } catch (JsonPatchException | JsonProcessingException e) {
      UUID errorId = UUID.randomUUID();
      log.error("Failed to apply patch. errorId: {} Exception: {}", errorId, e);
      throw new ErrorResponse("Failed to apply patch", errorId, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private ErrorResponse createErrorResponse(TransactionPO transaction, Exception e) {
    UUID errorId = UUID.randomUUID();
    log.error(
        "Could not save new transaction: {} Exception: {}, errorId: {}", transaction, e, errorId);
    return new ErrorResponse(
        "", errorId.toString(), "Could not create transaction", "", HttpStatus.SERVICE_UNAVAILABLE);
  }

  private ErrorResponse updateErrorResponse(TransactionPO transactionUpdate, Exception e) {
    UUID errorId = UUID.randomUUID();
    log.error(
        "Could not update transaction: {} Error: {} ErrorId: {}", transactionUpdate, e, errorId);
    return new ErrorResponse("", errorId.toString(), "", "", HttpStatus.SERVICE_UNAVAILABLE);
  }

  private void throwIfTransferInvalid(TransactionPO transaction) {
    if (isTransferAndAmountIsPositive(transaction)) {
      throw new ErrorResponse(
          "Amount cannot be positive for a transfer type transaction", HttpStatus.BAD_REQUEST);
    }
  }

  private boolean isTransferAndAmountIsPositive(TransactionPO transaction) {
    return !ObjectUtils.isEmpty(transaction.getDestinationAccount())
        && bigDecimalIsPositive(transaction.getAmount());
  }

  private boolean bigDecimalIsPositive(BigDecimal bigDecimal) {
    return bigDecimal.compareTo(BigDecimal.ZERO) > 0;
  }

  private TransactionPO saveOrErrorResponse(TransactionPO transaction) {
    try {
      return transactionsRepository.save(transaction);
    } catch (Exception e) {
      throw createErrorResponse(transaction, e);
    }
  }

  private void validateCreation(TransactionPO transaction) {
    throwIfContainsId(transaction);
    throwIfTransferInvalid(transaction);
    categoryValidator.validateCategories(transaction);
  }
}
