package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

import com.unosquare.carmigo.model.response.PassengerResponse;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/passengers")
@Tag(name = "Passenger Controller")
public class PassengerController {

  private final PassengerService passengerService;
  private final AppUser appUser;

  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<PassengerResponse> getCurrentPassengerProfile() {
    final var response = passengerService.getPassengerById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable final int passengerId) {
    final var response = passengerService.getPassengerById(getCurrentId(passengerId));
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<PassengerResponse> createPassenger() {
    final var response = passengerService.createPassengerById(getCurrentId(ALIAS_CURRENT_USER));
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping(value = "/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PassengerResponse> createPassengerById(@PathVariable final int passengerId) {
    final var response = passengerService.createPassengerById(getCurrentId(passengerId));
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteCurrentPassenger() {
    passengerService.deletePassengerById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.noContent().build();
  }

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
