package hu.readdeo.money.management.svc.securitymock;

import hu.readdeo.money.management.svc.security.model.User;
import hu.readdeo.money.management.svc.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
    implements WithSecurityContextFactory<WithMockCustomUser> {

  @Autowired UserRepository userRepository;

  @Override
  public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    User user = userRepository.findByUsername(customUser.username()).get();

    Authentication auth =
        new UsernamePasswordAuthenticationToken(user, customUser.password(), user.getAuthorities());
    context.setAuthentication(auth);
    SecurityContextHolder.getContext().setAuthentication(auth);

    return context;
  }
}
