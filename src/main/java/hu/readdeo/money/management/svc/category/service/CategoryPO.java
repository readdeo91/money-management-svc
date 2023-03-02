package hu.readdeo.money.management.svc.category.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.readdeo.money.management.svc.security.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.ObjectUtils;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "category", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPO {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore @ManyToOne private User user;
  @Column private String name;
  @Column private Long parentId;
  @Column private String color;

  public boolean isMainCategory() {
    return ObjectUtils.isEmpty(parentId);
  }
}
