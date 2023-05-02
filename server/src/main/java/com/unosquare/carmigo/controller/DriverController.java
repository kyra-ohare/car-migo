package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.model.response.DriverRequest;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.DriverService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/drivers")
@Tag(name = "Driver Controller")
public class DriverController {

  private final DriverService driverService;
  private final ModelMapper modelMapper;
  private final AppUser appUser;

  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<DriverRequest> getCurrentDriverProfile() {
    return ResponseEntity.ok(getDriver(ALIAS_CURRENT_USER));
  }

  @GetMapping(value = "/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<DriverRequest> getDriverById(@PathVariable final int driverId) {
    return ResponseEntity.ok(getDriver(driverId));
  }

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<DriverRequest> createDriver(
      @Valid @RequestBody final com.unosquare.carmigo.model.request.DriverRequest driverRequest) {
    return new ResponseEntity<>(createDriver(ALIAS_CURRENT_USER, driverRequest), HttpStatus.CREATED);
  }

  @PostMapping(value = "/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<DriverRequest> createDriverById(
      @PathVariable final int driverId, @Valid @RequestBody final com.unosquare.carmigo.model.request.DriverRequest driverRequest) {
    return new ResponseEntity<>(createDriver(driverId, driverRequest), HttpStatus.CREATED);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteCurrentDriver() {
    deleteDriver(ALIAS_CURRENT_USER);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "/{driverId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteDriverById(@PathVariable final int driverId) {
    deleteDriver(driverId);
    return ResponseEntity.noContent().build();
  }

  private DriverRequest getDriver(final int driverId) {
    final GrabDriverDTO grabDriverDTO = driverService.getDriverById(getCurrentId(driverId));
    return modelMapper.map(grabDriverDTO, DriverRequest.class);
  }

  private DriverRequest createDriver(final int driverId, final com.unosquare.carmigo.model.request.DriverRequest driverRequest) {
    final CreateDriverDTO createDriverDTO = modelMapper.map(driverRequest, CreateDriverDTO.class);
    final GrabDriverDTO grabDriverDTO = driverService.createDriverById(getCurrentId(driverId), createDriverDTO);
    return modelMapper.map(grabDriverDTO, DriverRequest.class);
  }

  private void deleteDriver(final int driverId) {
    driverService.deleteDriverById(getCurrentId(driverId));
  }

  private int getCurrentId(final int driverId) {
    return driverId == ALIAS_CURRENT_USER ? appUser.get().getId() : driverId;
  }
}
