package hu.readdeo.money.management.svc.transaction.service;

import hu.readdeo.money.management.svc.security.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface TransactionsRepository extends JpaRepository<TransactionPO, Long> {
  Page<TransactionPO> findByUserOrderByDateTimeDesc(User user, Pageable pageable);

  Boolean existsByUserAndId(User user, Long id);

  Optional<TransactionPO> findFirstByUserAndId(User user, Long id);

  void deleteByUserAndId(User user, Long id);
}
