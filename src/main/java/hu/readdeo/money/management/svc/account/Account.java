package hu.readdeo.money.management.svc.account;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Account {
  private Long id;

  @NotNull(message = "name missing")
  private String name = "";

  private String description = "";

  @NotNull(message = "currency missing")
  private String currency;

  @NotNull(message = "isCredit missing")
  private boolean isCredit;

  private BigDecimal initialCredit;
}
