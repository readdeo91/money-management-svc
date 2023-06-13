package hu.readdeo.money.management.svc.transaction.id;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import hu.readdeo.money.management.svc.account.service.AccountPO;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class TransactionCompositeId implements Serializable {

  @ManyToOne
  @NotNull(message = "sourceAccount missing")
  @JsonIdentityReference(alwaysAsId = true)
  private AccountPO sourceAccount;

  @Id private Long id;
}
