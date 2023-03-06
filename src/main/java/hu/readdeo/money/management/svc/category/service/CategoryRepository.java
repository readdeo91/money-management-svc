package hu.readdeo.money.management.svc.category.service;

import hu.readdeo.money.management.svc.security.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryPO, Long> {
  Optional<CategoryPO> findByUserAndId(User used, Long id);

  Boolean existsByUserAndId(User user, Long id);

  List<CategoryPO> findByUser(User used);

  void deleteByUserAndId(User user, Long id);
}
