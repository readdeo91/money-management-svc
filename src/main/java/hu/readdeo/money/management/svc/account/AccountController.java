package hu.readdeo.money.management.svc.account;

import com.github.fge.jsonpatch.JsonPatch;
import hu.readdeo.money.management.svc.account.service.AccountServiceAdapter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountServiceAdapter service;
  private final AccountModelAssembler accountModelAssembler;
  private final PagedResourcesAssembler<Account> pagedResourcesAssembler;

  @PostMapping
  public ResponseEntity<EntityModel<Account>> create(@Valid @RequestBody Account account) {
    Account createdAccount = service.create(account);
    EntityModel<Account> entityModel = accountModelAssembler.toModel(createdAccount);
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<Account>>> all() {
    List<Account> accounts = service.findAll();
    CollectionModel<EntityModel<Account>> accountsModel =
        accountModelAssembler.toCollectionModel(accounts);
    return ResponseEntity.ok(accountsModel);
  }

  @GetMapping(params = {"page", "size"})
  public ResponseEntity<CollectionModel<EntityModel<Account>>> page(Pageable pageable) {
    Page<Account> accountPage = service.getPage(pageable);
    PagedModel<EntityModel<Account>> pagedModel = pagedResourcesAssembler.toModel(accountPage);
    return ResponseEntity.ok().contentType(MediaTypes.HAL_JSON).body(pagedModel);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Account>> one(@PathVariable("id") @Valid Long id) {
    Account account = service.findById(id);
    EntityModel<Account> entityModel = accountModelAssembler.toModel(account);
    return ResponseEntity.ok(entityModel);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Account>> update(
      @PathVariable("id") @Valid Long id, @RequestBody Account accountUpdate) {
    Account updatedAccount = service.update(id, accountUpdate);
    EntityModel<Account> entityModel = accountModelAssembler.toModel(updatedAccount);
    return ResponseEntity.ok(entityModel);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<EntityModel<Account>> patch(
      @PathVariable("id") @Valid Long id, @RequestBody JsonPatch patch) {
    Account patchedAccount = service.patch(id, patch);
    EntityModel<Account> entityModel = accountModelAssembler.toModel(patchedAccount);
    return ResponseEntity.ok(entityModel);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") @Valid Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
