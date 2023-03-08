package hu.readdeo.money.management.svc.account;

import jakarta.validation.constraints.NotBlank;
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

  @NotBlank(message = "name missing")
  private String name = "";

  private String description = "";

  @NotBlank(message = "currency missing")
  private String currency;

  @NotBlank(message = "isCredit missing")
  private boolean isCredit;

  private BigDecimal initialCredit;
}
