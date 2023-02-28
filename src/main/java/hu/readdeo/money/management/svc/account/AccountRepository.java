package hu.readdeo.money.management.svc.account;

import hu.readdeo.money.management.svc.security.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
  Optional<Account> findFirstByUserAndId(User user, Long id);

  Boolean existsByUserAndId(User user, Long id);

  List<Account> findByUser(User user);

  List<Account> findByUserOrderById(User user, Pageable pageable);

  void deleteByUserAndId(User user, Long id);
}
