package hu.readdeo.money.management.svc.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.util.ObjectUtils;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Category {
  private Long id;

  @NotBlank(message = "name missing")
  private String name;

  private Long parentId;
  private String color;

  @JsonIgnore
  public boolean isMainCategory() {
    return ObjectUtils.isEmpty(parentId);
  }
}
