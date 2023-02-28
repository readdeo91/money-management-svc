package hu.readdeo.money.management.svc.security.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceRequest {
  @NotBlank
  @Size(max = 40)
  private String text;
}
