package hu.readdeo.money.management.svc.security.payload;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private Long id;
  private String username;
  private String name;
  private Instant joinedAt;
}
