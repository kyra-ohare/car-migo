package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.request.PlatformUserRequest;
import com.unosquare.carmigo.dto.response.PlatformUserResponse;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.PlatformUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "Platform User Controller")
public class PlatformUserController {

  private final PlatformUserService platformUserService;
  private final AppUser appUser;

  @PostMapping(value = "/confirm-email")
  public ResponseEntity<?> confirmEmail(@RequestParam final String email) {
    platformUserService.confirmEmail(email);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<PlatformUserResponse> getCurrentPlatformUserProfile() {
    final var response = platformUserService.getPlatformUserById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PlatformUserResponse> getPlatformUserById(@PathVariable final int userId) {
    final var response = platformUserService.getPlatformUserById(getCurrentId(userId));
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PlatformUserResponse> createPlatformUser(
      @Valid @RequestBody final PlatformUserRequest platformUserRequest) {
    final var response = platformUserService.createPlatformUser(platformUserRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PatchMapping(consumes = "application/json-patch+json")
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<PlatformUserResponse> patchCurrentPlatformUser(@RequestBody final JsonPatch patch) {
    final var response = platformUserService.patchPlatformUserById(getCurrentId(ALIAS_CURRENT_USER), patch);
    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
  }

  @PatchMapping(value = "/{userId}", consumes = "application/json-patch+json")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PlatformUserResponse> patchPlatformUserById(
      @PathVariable final int userId, @RequestBody final JsonPatch patch) {
    final var response = platformUserService.patchPlatformUserById(getCurrentId(userId), patch);
    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteCurrentPlatformUser() {
    platformUserService.deletePlatformUserById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "/{userId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> deletePlatformUserById(@PathVariable final int userId) {
    platformUserService.deletePlatformUserById(getCurrentId(userId));
    return ResponseEntity.noContent().build();
  }

  private int getCurrentId(final int userId) {
    return userId == ALIAS_CURRENT_USER ? appUser.get().getId() : userId;
  }
}
