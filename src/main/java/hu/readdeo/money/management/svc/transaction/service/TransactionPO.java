package hu.readdeo.money.management.svc.transaction.service;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.readdeo.money.management.svc.account.service.AccountPO;
import hu.readdeo.money.management.svc.category.service.CategoryPO;
import hu.readdeo.money.management.svc.security.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "transaction", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPO {
  @Id private UUID id;

  @ManyToOne
  @JsonIgnore
  @NotNull(message = "Unauthorized")
  private User user;

  @Column
  @NotNull(message = "amount missing")
  private BigDecimal amount;

  @Column
  @NotNull(message = "dateTime missing")
  private LocalDateTime dateTime;

  @Column private String description = "";

  @ManyToOne
  @NotNull(message = "sourceAccount missing")
  @JsonIdentityReference(alwaysAsId = true)
  private AccountPO sourceAccount;

  @ManyToOne
  @JsonIdentityReference(alwaysAsId = true)
  private AccountPO destinationAccount;

  @ManyToOne
  @NotNull(message = "mainCategory missing")
  @JsonIdentityReference(alwaysAsId = true)
  private CategoryPO mainCategory;

  @ManyToOne
  @JsonIdentityReference(alwaysAsId = true)
  private CategoryPO subCategory;
}
