package hu.readdeo.money.management.svc.transaction.service;

import com.github.fge.jsonpatch.JsonPatch;
import hu.readdeo.money.management.svc.transaction.Transaction;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionDTOAdapter {

  private final TransactionService service;
  private final TransactionMapper mapper;

  public Transaction create(Transaction transaction) {
    TransactionModel transactionModel = mapper.toTransactionModel(transaction);
    return mapper.toTransactionDTO(service.create(transactionModel));
  }

  public Transaction findById(UUID id) {
    return mapper.toTransactionDTO(service.findById(id));
  }

  public Page<Transaction> getPage(Pageable pageable) {
    Page<TransactionModel> transactionModelPage = service.getPage(pageable);
    return transactionModelPage.map(mapper::toTransactionDTO);
  }

  public Transaction patch(UUID id, JsonPatch patch) {
    return mapper.toTransactionDTO(service.patch(id, patch));
  }

  public Transaction update(UUID id, Transaction transactionUpdate) {
    TransactionModel transactionUpdateModel = mapper.toTransactionModel(transactionUpdate);
    return mapper.toTransactionDTO(service.update(id, transactionUpdateModel));
  }

  public void delete(UUID id) {
    service.delete(id);
  }
}
