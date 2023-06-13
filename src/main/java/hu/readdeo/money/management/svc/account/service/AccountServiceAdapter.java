package hu.readdeo.money.management.svc.account.service;

import com.github.fge.jsonpatch.JsonPatch;
import hu.readdeo.money.management.svc.account.Account;
import hu.readdeo.money.management.svc.transaction.service.TransactionService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceAdapter {

  private final AccountMapper mapper;
  private final AccountService service;
  private final TransactionService transactionService;

  public Account create(Account account) {
    AccountPO accountModel = mapper.toAccountModel(account);
    return mapper.toAccountDTO(service.create(accountModel));
  }

  public Account findById(Long id) {
    return mapper.toAccountDTO(service.findById(id));
  }

  public List<Account> findAll() {
    List<AccountPO> accounts = service.findAll();
    List<Account> accountDTOs = mapper.toAccountDTOList(accounts);
    for (int i = 0; i < accounts.size(); i++) {
      long balance = transactionService.accountBalance(accounts.get(i));
      accountDTOs.get(i).setBalance(BigDecimal.valueOf(balance));
    }
    return accountDTOs;
  }

  public Page<Account> getPage(Pageable pageable) {
    Page<AccountPO> accountModelPage = service.getPage(pageable);
    return accountModelPage.map(mapper::toAccountDTO);
  }

  public Account patch(Long id, JsonPatch patch) {
    return mapper.toAccountDTO(service.patch(id, patch));
  }

  public Account update(Long id, Account accountUpdate) {
    AccountPO accountUpdatePO = mapper.toAccountModel(accountUpdate);
    return mapper.toAccountDTO(service.update(id, accountUpdatePO));
  }

  public void delete(Long id) {
    service.delete(id);
  }
}
