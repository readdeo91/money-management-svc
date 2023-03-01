package hu.readdeo.money.management.svc.account.service;

import hu.readdeo.money.management.svc.security.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountPO, Long> {
  Optional<AccountPO> findFirstByUserAndId(User user, Long id);

  Boolean existsByUserAndId(User user, Long id);

  List<AccountPO> findByUser(User user);

  Page<AccountPO> findByUserOrderById(User user, Pageable pageable);

  void deleteByUserAndId(User user, Long id);
}
