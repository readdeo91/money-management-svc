package hu.readdeo.money.management.svc.transaction.id;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import hu.readdeo.money.management.svc.account.service.AccountPO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "transaction_id_counter", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class TransactionIdCounterPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "sourceAccount missing")
    @JsonIdentityReference(alwaysAsId = true)
    private AccountPO sourceAccount;

    private Long lastId;

    public Long getNextId() {
        return lastId++;
    }
}
