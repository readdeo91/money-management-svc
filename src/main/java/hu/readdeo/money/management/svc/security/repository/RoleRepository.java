package hu.readdeo.money.management.svc.security.repository;

import hu.readdeo.money.management.svc.security.model.Role;
import hu.readdeo.money.management.svc.security.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(RoleName roleName);
}
