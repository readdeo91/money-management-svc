package hu.readdeo.money.management.svc.security.controller;

import hu.readdeo.money.management.svc.security.CurrentUser;
import hu.readdeo.money.management.svc.security.exception.ResourceNotFoundException;
import hu.readdeo.money.management.svc.security.payload.User;
import hu.readdeo.money.management.svc.security.payload.UserIdentityAvailability;
import hu.readdeo.money.management.svc.security.payload.UserSummary;
import hu.readdeo.money.management.svc.security.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "user")
public class UserController {

  @Autowired private UserRepository userRepository;

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public UserSummary getCurrentUser(@CurrentUser User currentUser) {
    return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
  }

  @GetMapping("/checkUsernameAvailability")
  public UserIdentityAvailability checkUsernameAvailability(
      @RequestParam(value = "username") String username) {
    Boolean isAvailable = !userRepository.existsByUsername(username);
    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/checkEmailAvailability")
  public UserIdentityAvailability checkEmailAvailability(
      @RequestParam(value = "email") String email) {
    Boolean isAvailable = !userRepository.existsByEmail(email);
    return new UserIdentityAvailability(isAvailable);
  }

  @GetMapping("/{username}")
  public User getUserProfile(@PathVariable(value = "username") String username) {
    hu.readdeo.money.management.svc.security.model.User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    return new User(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt());
  }
}
