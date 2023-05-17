package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

import com.unosquare.carmigo.dto.response.PassengerResponse;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.PassengerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles Passenger APIs.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/passengers")
@Tag(name = "Passenger Controller")
public class PassengerController {

  private final PassengerService passengerService;
  private final AppUser appUser;

  /**
   * Enables logged-in users to see their profiles.
   * @return a {@link PassengerResponse}.
   */
  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<PassengerResponse> getCurrentPassengerProfile() {
    final var response = passengerService.getPassengerById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in admin users to see other user's profiles.
   * @param passengerId the passenger's id.
   * @return a {@link PassengerResponse}.
   */
  @GetMapping(value = "/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable final int passengerId) {
    final var response = passengerService.getPassengerById(getCurrentId(passengerId));
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in user to have a passenger's profile.
   * @return a {@link PassengerResponse}.
   */
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<PassengerResponse> createPassenger() {
    final var response = passengerService.createPassengerById(getCurrentId(ALIAS_CURRENT_USER));
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Enables logged-in admin users to create a passenger's profile for another user.
   * @param passengerId the user to become a passenger.
   * @return a {@link PassengerResponse}.
   */
  @PostMapping(value = "/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PassengerResponse> createPassengerById(@PathVariable final int passengerId) {
    final var response = passengerService.createPassengerById(getCurrentId(passengerId));
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Delete logged-in user's passenger's profile.
   * @return an empty body.
   */
  @DeleteMapping
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteCurrentPassenger() {
    passengerService.deletePassengerById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.noContent().build();
  }

  /**
   * Enables logged-in admin users to delete a passenger's profile.
   * @param passengerId the user to become a passenger.
   * @return an empty body.
   */
  @DeleteMapping(value = "/{passengerId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> deletePassengerById(@PathVariable final int passengerId) {
    passengerService.deletePassengerById(getCurrentId(passengerId));
    return ResponseEntity.noContent().build();
  }

  private int getCurrentId(final int passengerId) {
    return passengerId == ALIAS_CURRENT_USER ? appUser.get().getId() : passengerId;
  }
}
