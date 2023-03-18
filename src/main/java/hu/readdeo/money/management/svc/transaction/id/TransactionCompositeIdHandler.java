package hu.readdeo.money.management.svc.transaction.id;

import hu.readdeo.money.management.svc.transaction.service.TransactionPO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionCompositeIdHandler {
    private final TransactionIdCounterRepository idCounterRepository;

    public void setIdForNewTransaction(TransactionPO transaction) {
        Long nextId = getNextId(transaction);
        transaction.setId(nextId);
    }

    private Long getNextId(TransactionPO transaction) {
        TransactionIdCounterPO counter = getCounter(transaction);
        Long nextId = counter.getNextId();
        idCounterRepository.save(counter);
        return nextId;
    }

    private TransactionIdCounterPO getCounter(TransactionPO transaction) {
        TransactionIdCounterPO counter = idCounterRepository.findBySourceAccount(transaction.getSourceAccount());
        if (counter == null){
            counter = createCounterIfNull(transaction);
        }
        return counter;
    }

    private TransactionIdCounterPO createCounterIfNull(TransactionPO transaction) {
        TransactionIdCounterPO counter = new TransactionIdCounterPO();
            counter.setSourceAccount(transaction.getSourceAccount());
            counter.setLastId(0L);
            idCounterRepository.save(counter);
            return counter;
    }

}
