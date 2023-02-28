package hu.readdeo.money.management.svc.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
  private UUID id;

  @NotNull(message = "amount missing")
  private BigDecimal amount;

  @NotNull(message = "dateTime missing")
  private LocalDateTime dateTime;

  private String description = "";

  @NotNull(message = "sourceAccount missing")
  private Long sourceAccount;

  private Long destinationAccount;

  @NotNull(message = "mainCategory missing")
  private Long mainCategory;

  private Long subCategory;
}
