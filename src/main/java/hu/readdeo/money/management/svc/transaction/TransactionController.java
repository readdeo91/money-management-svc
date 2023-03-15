package hu.readdeo.money.management.svc.transaction;

import com.github.fge.jsonpatch.JsonPatch;
import hu.readdeo.money.management.svc.transaction.service.TransactionServiceAdapter;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionServiceAdapter serviceAdapter;
  private final TransactionModelAssembler transactionModelAssembler;
  private final PagedResourcesAssembler<Transaction> pagedResourcesAssembler;

  @PostMapping
  public ResponseEntity<EntityModel<Transaction>> create(
      @Valid @RequestBody Transaction transaction) {
    Transaction createdTransaction = serviceAdapter.create(transaction);
    EntityModel<Transaction> entityModel = transactionModelAssembler.toModel(createdTransaction);
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<Transaction>>> page(Pageable pageable) {
    Page<Transaction> transactionsPage = serviceAdapter.getPage(pageable);
    PagedModel<EntityModel<Transaction>> pagedModel =
        pagedResourcesAssembler.toModel(transactionsPage, transactionModelAssembler);
    return ResponseEntity.ok().contentType(MediaTypes.HAL_JSON).body(pagedModel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Transaction>> one(@PathVariable("id") UUID id) {
    Transaction transaction = serviceAdapter.findById(id);
    EntityModel<Transaction> entityModel = transactionModelAssembler.toModel(transaction);
    return ResponseEntity.ok(entityModel);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Transaction>> update(
      @PathVariable("id") UUID id, @Valid @RequestBody Transaction transactionUpdate) {
    Transaction updatedTransaction = serviceAdapter.update(id, transactionUpdate);
    EntityModel<Transaction> entityModel = transactionModelAssembler.toModel(updatedTransaction);
    return ResponseEntity.ok(entityModel);
  }

  @PatchMapping("/")
  public ResponseEntity<Void> patch() {
    return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<EntityModel<Transaction>> patch(
      @PathVariable("id") @Valid UUID id, @Valid @RequestBody JsonPatch patch) {
    Transaction patchedTransaction = serviceAdapter.patch(id, patch);
    EntityModel<Transaction> entityModel = transactionModelAssembler.toModel(patchedTransaction);
    return ResponseEntity.ok(entityModel);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
    serviceAdapter.delete(id);
    return ResponseEntity.noContent().build();
  }
}
