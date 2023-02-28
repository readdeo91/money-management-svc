package hu.readdeo.money.management.svc.security.util;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
  Authentication getAuthentication();
}
