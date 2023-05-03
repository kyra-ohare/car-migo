package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

import com.unosquare.carmigo.model.request.DriverRequest;
import com.unosquare.carmigo.model.response.DriverResponse;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.DriverService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/drivers")
@Tag(name = "Driver Controller")
public class DriverController {

  private final DriverService driverService;
  private final AppUser appUser;

  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<DriverResponse> getCurrentDriverProfile() {
    final var response = driverService.getDriverById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<DriverResponse> getDriverById(@PathVariable final int driverId) {
    final var response = driverService.getDriverById(getCurrentId(driverId));
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<DriverResponse> createDriver(
    @Valid @RequestBody final com.unosquare.carmigo.model.request.DriverRequest driverRequest) {
    final var response = driverService.createDriverById(getCurrentId(ALIAS_CURRENT_USER), driverRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping(value = "/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<DriverResponse> createDriverById(
    @PathVariable final int driverId, @Valid @RequestBody final DriverRequest driverRequest) {
    final var response = driverService.createDriverById(getCurrentId(driverId), driverRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteCurrentDriver() {
    driverService.deleteDriverById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "/{driverId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteDriverById(@PathVariable final int driverId) {
    driverService.deleteDriverById(getCurrentId(driverId));
    return ResponseEntity.noContent().build();
  }

  private int getCurrentId(final int driverId) {
    return driverId == ALIAS_CURRENT_USER ? appUser.get().getId() : driverId;
  }
}
