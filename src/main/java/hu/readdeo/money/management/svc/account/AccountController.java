package hu.readdeo.money.management.svc.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;
  private final AccountModelAssembler accountModelAssembler;

  @PostMapping
  public ResponseEntity<EntityModel<Account>> create(@RequestBody Account account) {
    EntityModel<Account> entityModel =
        accountModelAssembler.toModel(accountService.create(account));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<Account>>> all() {
    List<Account> accounts = accountService.findAll();
    return new ResponseEntity<>(accountModelAssembler.toCollectionModel(accounts), HttpStatus.OK);
  }

  @GetMapping(params = {"page", "size"})
  public ResponseEntity<CollectionModel<EntityModel<Account>>> page(
      @RequestParam("page") int page, @RequestParam("size") int size) {
    List<Account> accounts = accountService.getPage(page, size);
    return new ResponseEntity<>(
        accountModelAssembler.toPageModel(page, size, accounts), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Account>> one(@PathVariable("id") @Valid Long id) {
    return new ResponseEntity<>(
        accountModelAssembler.toModel(accountService.findById(id)), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Account>> update(
      @PathVariable("id") @Valid Long id, @RequestBody Account updatedAccount) {
    return new ResponseEntity<>(
        accountModelAssembler.toModel(accountService.update(id, updatedAccount)), HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<EntityModel<Account>> patch(
      @PathVariable("id") @Valid Long id, @RequestBody JsonPatch patch) {
    try {
      Account patchedTransaction = accountService.applyPatch(patch, id);
      accountService.update(id, patchedTransaction);
      return ResponseEntity.ok(
          accountModelAssembler.toModel(accountService.update(id, patchedTransaction)));
    } catch (JsonPatchException | JsonProcessingException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") @Valid Long id) {
    accountService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
