package hu.readdeo.money.management.svc.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import hu.readdeo.money.management.svc.exception.ErrorResponse;
import hu.readdeo.money.management.svc.security.model.User;
import hu.readdeo.money.management.svc.security.util.AuthenticationFacade;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
  private final ObjectMapper objectMapper;
  private final AccountRepository accountsRepository;
  private final AuthenticationFacade authenticationFacade;

  @Value("${account.max.page.size:100}")
  int maxPageSize;

  public Account create(Account account) {
    throwIfContainsId(account);
    setUser(account);
    try {
      return accountsRepository.save(account);
    } catch (Exception e) {
      throw createErrorResponse(account, e);
    }
  }

  public Account findById(Long id) {
    User user = authenticationFacade.getUser();
    Optional<Account> accountOptional = accountsRepository.findFirstByUserAndId(user, id);
    throwIfNotFound(accountOptional);
    return accountOptional.get();
  }

  public List<Account> findAll() {
    User user = authenticationFacade.getUser();
    List<Account> accountList = accountsRepository.findByUser(user);
    throwIfNotFound(accountList);
    return accountList;
  }

  public List<Account> getPage(int page, int size) {
    User user = authenticationFacade.getUser();
    size = limitSizeToMaxValue(size);
    Pageable pageable = PageRequest.of(page, size);
    List<Account> accountList = accountsRepository.findByUserOrderById(user, pageable);
    throwIfNotFound(accountList);
    return accountList;
  }

  public Account update(Long id, Account accountUpdate) {
    throwIfDoesntExist(id);
    setIdAndUser(id, accountUpdate);
    try {
      return accountsRepository.save(accountUpdate);
    } catch (Exception e) {
      throw updateErrorResponse(accountUpdate, e);
    }
  }

  @Transactional
  public void delete(Long id) {
    User user = authenticationFacade.getUser();
    accountsRepository.deleteByUserAndId(user, id);
  }

  private void setUser(Account account) {
    User user = authenticationFacade.getUser();
    account.setUser(user);
  }

  private void throwIfContainsId(Account account) {
    if (!ObjectUtils.isEmpty(account.getId())) {
      throw new ErrorResponse(
          null, null, "New entry cannot contain ID", null, HttpStatus.BAD_REQUEST);
    }
  }

  private void setIdAndUser(Long id, Account accountUpdate) {
    User user = authenticationFacade.getUser();
    accountUpdate.setId(id);
    accountUpdate.setUser(user);
  }

  private void throwIfNotFound(Optional<Account> accountObject) {
    if (ObjectUtils.isEmpty(accountObject)) {
      throw new ErrorResponse("Account not found", HttpStatus.NOT_FOUND);
    }
  }

  private void throwIfNotFound(List<Account> accountList) {
    if (ObjectUtils.isEmpty(accountList)) {
      throw new ErrorResponse("No accounts found", HttpStatus.NOT_FOUND);
    }
  }

  private void throwIfDoesntExist(Long id) {
    User user = authenticationFacade.getUser();
    if (!accountsRepository.existsByUserAndId(user, id)) {
      throw new ErrorResponse("Account not found", HttpStatus.NOT_FOUND);
    }
  }

  private int limitSizeToMaxValue(int size) {
    if (size > maxPageSize) {
      size = maxPageSize;
    }
    return size;
  }

  public Account applyPatch(JsonPatch patch, Long id)
      throws JsonPatchException, JsonProcessingException {
    Account targetAccount = findById(id);
    JsonNode patched = patch.apply(objectMapper.convertValue(targetAccount, JsonNode.class));
    return objectMapper.treeToValue(patched, Account.class);
  }

  private ErrorResponse createErrorResponse(Account account, Exception e) {
    UUID errorId = UUID.randomUUID();
    log.error(
        "Could not save new account: {} error: {}, errorId: {}", account, e.getMessage(), errorId);
    return new ErrorResponse(
        null,
        errorId.toString(),
        "Could not create new account",
        null,
        HttpStatus.SERVICE_UNAVAILABLE);
  }

  private ErrorResponse updateErrorResponse(Account accountUpdate, Exception e) {
    UUID errorId = UUID.randomUUID();
    log.error(
        "Could not update account: {} Error: {} ErrorId: {}",
        accountUpdate,
        e.getMessage(),
        errorId);
    return new ErrorResponse(
        null,
        errorId.toString(),
        "Could not update account",
        null,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
