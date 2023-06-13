package hu.readdeo.money.management.svc.transaction.id;

import hu.readdeo.money.management.svc.account.service.AccountPO;
import org.springframework.data.jpa.repository.JpaRepository;

interface TransactionIdCounterRepository extends JpaRepository<TransactionIdCounterPO, Long> {

  TransactionIdCounterPO findBySourceAccount(AccountPO sourceAccount);
}
