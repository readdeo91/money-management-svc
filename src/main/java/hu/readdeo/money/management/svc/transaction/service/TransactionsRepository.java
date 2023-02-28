package hu.readdeo.money.management.svc.transaction.service;

import hu.readdeo.money.management.svc.security.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface TransactionsRepository extends JpaRepository<TransactionModel, UUID> {
  Page<TransactionModel> findByUserOrderByDateTimeDesc(User user, Pageable pageable);

  Boolean existsByUserAndId(User user, UUID id);

  Optional<TransactionModel> findFirstByUserAndId(User user, UUID id);

  void deleteByUserAndId(User user, UUID id);
}
