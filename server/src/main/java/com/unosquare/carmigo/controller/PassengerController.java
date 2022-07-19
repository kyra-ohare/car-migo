package com.unosquare.carmigo.controller;

import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.model.response.PassengerViewModel;
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
import org.springframework.web.bind.annotation.ResponseStatus;
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
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PassengerViewModel> getCurrentPassengerProfile() {
    return ResponseEntity.ok(getPassenger(0));
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PassengerViewModel> getPassengerById(@PathVariable final int id) {
    return ResponseEntity.ok(getPassenger(id));
  }

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PassengerViewModel> createPassenger() {
    return new ResponseEntity<>(createPassenger(0), HttpStatus.CREATED);
  }

  @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PassengerViewModel> createPassengerById(@PathVariable final int id) {
    return new ResponseEntity<>(createPassenger(id), HttpStatus.CREATED);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deleteCurrentPassenger() {
    deletePassenger(0);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deletePassengerById(@PathVariable final int id) {
    deletePassenger(id);
    return ResponseEntity.noContent().build();
  }

  private PassengerViewModel getPassenger(final int id) {
    final GrabPassengerDTO grabPassengerDTO = passengerService.getPassengerById(getCurrentId(id));
    return modelMapper.map(grabPassengerDTO, PassengerViewModel.class);
  }

  private PassengerViewModel createPassenger(final int id) {
    final GrabPassengerDTO grabPassengerDTO = passengerService.createPassengerById(getCurrentId(id));
    return modelMapper.map(grabPassengerDTO, PassengerViewModel.class);
  }

  private void deletePassenger(final int id) {
    passengerService.deletePassengerById(getCurrentId(id));
  }

  private int getCurrentId(final int id) {
    return id != 0 ? id : appUser.get().getId();
  }
}
