package com.unosquare.carmigo.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.model.request.CreateDriverViewModel;
import com.unosquare.carmigo.model.response.DriverViewModel;
import com.unosquare.carmigo.model.response.PassengerViewModel;
import com.unosquare.carmigo.model.response.PlatformUserViewModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/users")
@Tag(name = "Platform User Admin Controller")
public class UserAdminController {

  private final UserControllerHelper userControllerHelper;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PlatformUserViewModel> getPlatformUserById(@PathVariable final int id) {
    return ResponseEntity.ok(userControllerHelper.getPlatformUserById(id));
  }

  @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<PlatformUserViewModel> patchPlatformUserById(
      @PathVariable final int id, @RequestBody final JsonPatch patch) {
    return new ResponseEntity<>(userControllerHelper.patchPlatformUserById(id, patch), HttpStatus.ACCEPTED);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deletePlatformUserById(@PathVariable final int id) {
    userControllerHelper.deletePlatformUserById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/drivers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<DriverViewModel> getDriverById(@PathVariable final int id) {
    return ResponseEntity.ok(userControllerHelper.getDriverById(id));
  }

  @PostMapping(value = "/{id}/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<DriverViewModel> createDriver(
      @PathVariable final int id, @Valid @RequestBody final CreateDriverViewModel createDriverViewModal) {
    final DriverViewModel driverViewModel = userControllerHelper.createDriverById(id, createDriverViewModal);
    return new ResponseEntity<>(driverViewModel, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/drivers/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deleteDriverById(@PathVariable final int id) {
    userControllerHelper.deleteDriverById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/passengers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PassengerViewModel> getPassengerById(@PathVariable final int id) {
    return ResponseEntity.ok(userControllerHelper.getPassengerById(id));
  }

  @PostMapping(value = "/{id}/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PassengerViewModel> createPassenger(@PathVariable final int id) {
    final PassengerViewModel passengerViewModel = userControllerHelper.createPassengerById(id);
    return new ResponseEntity<>(passengerViewModel, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/passengers/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deletePassengerById(@PathVariable final int id) {
    userControllerHelper.deletePassengerById(id);
    return ResponseEntity.noContent().build();
  }
}
