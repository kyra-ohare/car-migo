package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

import com.unosquare.carmigo.dto.request.DriverRequest;
import com.unosquare.carmigo.dto.response.DriverResponse;
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

/**
 * Handles Driver APIs.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/drivers")
@Tag(name = "Driver Controller")
public class DriverController {

  private final DriverService driverService;
  private final AppUser appUser;

  /**
   * Enables logged-in users to see their profiles.
   * @return a {@link DriverResponse}.
   */
  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<DriverResponse> getCurrentDriverProfile() {
    final var response = driverService.getDriverById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in admin users to see other user's profiles.
   * @param driverId the driver's id.
   * @return a {@link DriverResponse}.
   */
  @GetMapping(value = "/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<DriverResponse> getDriverById(@PathVariable final int driverId) {
    final var response = driverService.getDriverById(getCurrentId(driverId));
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in user to have a driver's profile.
   * @param driverRequest Request body as {@link DriverRequest}.
   * @return a {@link DriverResponse}.
   */
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<DriverResponse> createDriver(
      @Valid @RequestBody final DriverRequest driverRequest) {
    final var response = driverService.createDriverById(getCurrentId(ALIAS_CURRENT_USER), driverRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Enables logged-in admin users to create a driver's profile for another user.
   * @param driverId the user to become a driver.
   * @param driverRequest Request body as {@link DriverRequest}.
   * @return a {@link DriverResponse}.
   */
  @PostMapping(value = "/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<DriverResponse> createDriverById(
      @PathVariable final int driverId, @Valid @RequestBody final DriverRequest driverRequest) {
    final var response = driverService.createDriverById(getCurrentId(driverId), driverRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Delete logged-in user's driver's profile.
   * @return an empty body.
   */
  @DeleteMapping
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteCurrentDriver() {
    driverService.deleteDriverById(getCurrentId(ALIAS_CURRENT_USER));
    return ResponseEntity.noContent().build();
  }

  /**
   * Enables logged-in admin users to delete a driver's profile.
   * @param driverId the driver's id to be deleted.
   * @return an empty body.
   */
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
