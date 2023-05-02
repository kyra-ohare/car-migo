package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.model.response.PassengerRequest;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.PassengerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
  private final ModelMapper modelMapper;
  private final AppUser appUser;

  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<PassengerRequest> getCurrentPassengerProfile() {
    return ResponseEntity.ok(getPassenger(ALIAS_CURRENT_USER));
  }

  @GetMapping(value = "/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PassengerRequest> getPassengerById(@PathVariable final int passengerId) {
    return ResponseEntity.ok(getPassenger(passengerId));
  }

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<PassengerRequest> createPassenger() {
    return new ResponseEntity<>(createPassenger(ALIAS_CURRENT_USER), HttpStatus.CREATED);
  }

  @PostMapping(value = "/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PassengerRequest> createPassengerById(@PathVariable final int passengerId) {
    return new ResponseEntity<>(createPassenger(passengerId), HttpStatus.CREATED);
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteCurrentPassenger() {
    deletePassenger(ALIAS_CURRENT_USER);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "/{passengerId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> deletePassengerById(@PathVariable final int passengerId) {
    deletePassenger(passengerId);
    return ResponseEntity.noContent().build();
  }

  private PassengerRequest getPassenger(final int passengerId) {
    final GrabPassengerDTO grabPassengerDTO = passengerService.getPassengerById(getCurrentId(passengerId));
    return modelMapper.map(grabPassengerDTO, PassengerRequest.class);
  }

  private PassengerRequest createPassenger(final int passengerId) {
    final GrabPassengerDTO grabPassengerDTO = passengerService.createPassengerById(getCurrentId(passengerId));
    return modelMapper.map(grabPassengerDTO, PassengerRequest.class);
  }

  private void deletePassenger(final int passengerId) {
    passengerService.deletePassengerById(getCurrentId(passengerId));
  }

  private int getCurrentId(final int passengerId) {
    return passengerId == ALIAS_CURRENT_USER ? appUser.get().getId() : passengerId;
  }
}
