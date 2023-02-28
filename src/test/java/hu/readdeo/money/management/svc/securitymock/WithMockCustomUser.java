package hu.readdeo.money.management.svc.securitymock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
  String username() default "readdeo";

  String password() default "$2a$10$0Bxh9jqp2BhEzkCiGOWtTeAUg7/YZ9oPw8y5P/3khYG..2RQAkmAu";
}
