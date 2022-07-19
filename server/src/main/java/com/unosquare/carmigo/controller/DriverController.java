package com.unosquare.carmigo.controller;

import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.model.request.CreateDriverViewModel;
import com.unosquare.carmigo.model.response.DriverViewModel;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.DriverService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
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
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<DriverViewModel> getCurrentDriverProfile() {
    return ResponseEntity.ok(getDriver(0));
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<DriverViewModel> getDriverById(@PathVariable final int id) {
    return ResponseEntity.ok(getDriver(id));
  }

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<DriverViewModel> createDriver(
      @Valid @RequestBody final CreateDriverViewModel createDriverViewModel) {
    return new ResponseEntity<>(createDriver(0, createDriverViewModel), HttpStatus.CREATED);
  }

  @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<DriverViewModel> createDriverById(
      @PathVariable final int id, @Valid @RequestBody final CreateDriverViewModel createDriverViewModel) {
    return new ResponseEntity<>(createDriver(id, createDriverViewModel), HttpStatus.CREATED);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deleteCurrentDriver() {
    deleteDriver(0);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deleteDriverById(@PathVariable final int id) {
    deleteDriver(id);
    return ResponseEntity.noContent().build();
  }

  private DriverViewModel getDriver(final int id) {
    final GrabDriverDTO grabDriverDTO = driverService.getDriverById(getCurrentId(id));
    return modelMapper.map(grabDriverDTO, DriverViewModel.class);
  }

  private DriverViewModel createDriver(final int id, final CreateDriverViewModel createDriverViewModel) {
    final CreateDriverDTO createDriverDTO = modelMapper.map(createDriverViewModel, CreateDriverDTO.class);
    final GrabDriverDTO grabDriverDTO = driverService.createDriverById(getCurrentId(id), createDriverDTO);
    return modelMapper.map(grabDriverDTO, DriverViewModel.class);
  }

  private void deleteDriver(final int id) {
    driverService.deleteDriverById(getCurrentId(id));
  }

  private int getCurrentId(final int id) {
    return id != 0 ? id : appUser.get().getId();
  }
}
