package hu.readdeo.money.management.svc.transaction.service;

import com.github.fge.jsonpatch.JsonPatch;
import hu.readdeo.money.management.svc.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceAdapter {

  private final TransactionService service;
  private final TransactionMapper mapper;

  public Transaction create(Transaction transaction) {
    TransactionPO transactionModel = mapper.toTransactionModel(transaction);
    return mapper.toTransactionDTO(service.create(transactionModel));
  }

  public Transaction findById(Long id) {
    return mapper.toTransactionDTO(service.findById(id));
  }

  public Page<Transaction> getPage(Pageable pageable) {
    Page<TransactionPO> transactionModelPage = service.getPage(pageable);
    return transactionModelPage.map(mapper::toTransactionDTO);
  }

  public Transaction patch(Long id, JsonPatch patch) {
    return mapper.toTransactionDTO(service.patch(id, patch));
  }

  public Transaction update(Long id, Transaction transactionUpdate) {
    TransactionPO transactionUpdateModel = mapper.toTransactionModel(transactionUpdate);
    return mapper.toTransactionDTO(service.update(id, transactionUpdateModel));
  }

  public void delete(Long id) {
    service.delete(id);
  }
}
