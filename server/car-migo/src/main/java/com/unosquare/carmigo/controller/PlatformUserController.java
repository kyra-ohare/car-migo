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

/**
 * Handles Platform User APIs.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "Platform User Controller")
public class PlatformUserController {

  private final PlatformUserService platformUserService;
  private final AppUser appUser;

  /**
   * Enables a user to create an account. This new user's access status is set to STAGED.
   *
   * @param platformUserRequest Request body as {@link PlatformUserRequest}.
   * @return Response body as {@link PlatformUserResponse}.
   */
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PlatformUserResponse> createPlatformUser(
      @Valid @RequestBody final PlatformUserRequest platformUserRequest) {
    final var response = platformUserService.createPlatformUser(platformUserRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Enables user to confirm their email after creating an account.
   * Upon confirmation, their access status is set to ACTIVE.
   *
   * @param email the email used to create an account.
   * @return an empty body.
   */
  @PostMapping(value = "/confirm-email")
  public ResponseEntity<?> confirmEmail(@RequestParam final String email) {
    platformUserService.confirmEmail(email);
    return ResponseEntity.ok().build();
  }

  /**
   * Enables logged-in users to see their profiles.
   *
   * @return Response body as {@link PlatformUserResponse}.
   */
  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<PlatformUserResponse> getCurrentPlatformUserProfile() {
    final var response = platformUserService.getPlatformUserById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in admin users to see other user's profiles.
   *
   * @param platformUserId the platform user's id.
   * @return Response body as {@link PlatformUserResponse}.
   */
  @GetMapping(value = "/{platformUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PlatformUserResponse> getPlatformUserById(@PathVariable final int platformUserId) {
    final var response = platformUserService.getPlatformUserById(getCurrentId(platformUserId));
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in users to correct their profiles.
   *
   * @param patch Request body as {@link JsonPatch}.
   * @return Response body as {@link PlatformUserResponse}.
   */
  @PatchMapping(consumes = "application/json-patch+json")
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<PlatformUserResponse> patchCurrentPlatformUser(@RequestBody final JsonPatch patch) {
    final var response = platformUserService.patchPlatformUserById(getCurrentId(ALIAS_CURRENT_USER), patch);
    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
  }

  /**
   * Enables admin logged-in users to correct other user's profiles.
   *
   * @param platformUserId the platform user's id.
   * @param patch          Request body as {@link JsonPatch}.
   * @return Response body as {@link PlatformUserResponse}.
   */
  @PatchMapping(value = "/{platformUserId}", consumes = "application/json-patch+json")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PlatformUserResponse> patchPlatformUserById(
      @PathVariable final int platformUserId, @RequestBody final JsonPatch patch) {
    final var response = platformUserService.patchPlatformUserById(getCurrentId(platformUserId), patch);
    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
  }

  /**
   * Enables logged-in user's to delete their profile.
   *
   * @return an empty body.
   */
  @DeleteMapping
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteCurrentPlatformUser() {
    platformUserService.deletePlatformUserById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.noContent().build();
  }

  /**
   * Enables logged-in admin users to delete a user's profile.
   *
   * @param platformUserId the platform user's id.
   * @return an empty body.
   */
  @DeleteMapping(value = "/{platformUserId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> deletePlatformUserById(@PathVariable final int platformUserId) {
    platformUserService.deletePlatformUserById(getCurrentId(platformUserId));
    return ResponseEntity.noContent().build();
  }

  private int getCurrentId(final int userId) {
    return userId == ALIAS_CURRENT_USER ? appUser.get().getId() : userId;
  }
}
