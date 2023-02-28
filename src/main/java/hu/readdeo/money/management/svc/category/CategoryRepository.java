package hu.readdeo.money.management.svc.category;

import hu.readdeo.money.management.svc.security.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByUserAndId(User used, Long id);

  Boolean existsByUserAndId(User user, Long id);

  List<Category> findByUser(User used);

  Optional<Category> findByIdAndUser(Long id, User user);

  void deleteByIdAndUser(Long id, User user);
}
