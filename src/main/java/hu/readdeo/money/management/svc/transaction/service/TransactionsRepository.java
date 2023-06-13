package hu.readdeo.money.management.svc.transaction.service;

import hu.readdeo.money.management.svc.account.service.AccountPO;
import hu.readdeo.money.management.svc.security.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface TransactionsRepository extends JpaRepository<TransactionPO, Long> {
  Page<TransactionPO> findByUserOrderByDateTimeDesc(User user, Pageable pageable);

  Page<TransactionPO> findByUserOrderByDateTimeAsc(User user, Pageable pageable);

  Boolean existsByUserAndId(User user, Long id);

  Optional<TransactionPO> findFirstByUserAndId(User user, Long id);

  void deleteByUserAndId(User user, Long id);

  @Query("SELECT sum(t.amount) from TransactionPO t WHERE t.sourceAccount = :account")
  long accountBalance(AccountPO account);
}
