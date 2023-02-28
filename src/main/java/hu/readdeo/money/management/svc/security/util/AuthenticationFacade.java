package hu.readdeo.money.management.svc.security.util;

import hu.readdeo.money.management.svc.security.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
  public User getUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
