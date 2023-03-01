package hu.readdeo.money.management.svc.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MethodNameLogger {

  @Value("${logging.methodnames}")
  private boolean logMethodNames;

  @Pointcut(
      "within(hu.readdeo.money.management.svc.account..*) ||"
          + "within(hu.readdeo.money.management.svc.category..*) ||"
          + "within(hu.readdeo.money.management.svc.transaction..*)")
  private void everythingInMyApplication() {}

  @Before("hu.readdeo.money.management.svc.util.MethodNameLogger.everythingInMyApplication()")
  public void logMethodName(JoinPoint joinPoint) {
    final String[] classFullName = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
    final String className = classFullName[classFullName.length - 1];
    if (logMethodNames) {
      log.debug("Called {}.{}", className, joinPoint.getSignature().getName());
    }
  }
}
