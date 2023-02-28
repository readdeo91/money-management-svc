package hu.readdeo.money.management.svc.security.controller;

import hu.readdeo.money.management.svc.security.JwtTokenProvider;
import hu.readdeo.money.management.svc.security.exception.AppException;
import hu.readdeo.money.management.svc.security.model.Role;
import hu.readdeo.money.management.svc.security.model.RoleName;
import hu.readdeo.money.management.svc.security.model.User;
import hu.readdeo.money.management.svc.security.payload.ApiResponse;
import hu.readdeo.money.management.svc.security.payload.JwtAuthenticationResponse;
import hu.readdeo.money.management.svc.security.payload.LoginRequest;
import hu.readdeo.money.management.svc.security.payload.SignUpRequest;
import hu.readdeo.money.management.svc.security.repository.RoleRepository;
import hu.readdeo.money.management.svc.security.repository.UserRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired AuthenticationManager authenticationManager;
  @Autowired UserRepository userRepository;
  @Autowired RoleRepository roleRepository;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired JwtTokenProvider tokenProvider;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return new ResponseEntity(
          new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return new ResponseEntity(
          new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
    }

    User createdUser = createAndSaveUser(signUpRequest);

    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/users/{username}")
            .buildAndExpand(createdUser.getUsername())
            .toUri();

    return ResponseEntity.created(location)
        .body(new ApiResponse(true, "User registered successfully"));
  }

  private User createAndSaveUser(SignUpRequest signUpRequest) {
    User user =
        new User(
            signUpRequest.getName(),
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            signUpRequest.getPassword());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    Role userRole =
        roleRepository
            .findByName(RoleName.ROLE_USER)
            .orElseThrow(() -> new AppException("User Role not set."));
    user.setRoles(Collections.singleton(userRole));
    return userRepository.save(user);
  }
}
