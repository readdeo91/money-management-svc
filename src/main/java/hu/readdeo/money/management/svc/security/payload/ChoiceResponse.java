package hu.readdeo.money.management.svc.security.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceResponse {
  private long id;
  private String text;
  private long voteCount;
}
