package com.unosquare.carmigo.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.dto.GrabPlatformUserDTO;
import com.unosquare.carmigo.model.request.CreateDriverViewModel;
import com.unosquare.carmigo.model.response.DriverViewModel;
import com.unosquare.carmigo.model.response.PassengerViewModel;
import com.unosquare.carmigo.model.response.PlatformUserViewModel;
import com.unosquare.carmigo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
@Tag(name = "Admin User Controller")
public class AdminUserController {

  private final ModelMapper modelMapper;
  private final UserService userService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PlatformUserViewModel> getPlatformUserById(@PathVariable final int id) {
    final GrabPlatformUserDTO grabPlatformUserDTO = userService.getPlatformUserById(id);
    final PlatformUserViewModel platformUserViewModel = modelMapper.map(
        grabPlatformUserDTO, PlatformUserViewModel.class);
    return ResponseEntity.ok(platformUserViewModel);
  }

  @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<PlatformUserViewModel> patchPlatformUserById(
      @PathVariable final int id, @RequestBody final JsonPatch patch) {
    final GrabPlatformUserDTO grabPlatformUserDTO = userService.patchPlatformUserById(id, patch);
    final PlatformUserViewModel platformUserViewModel = modelMapper.map(
        grabPlatformUserDTO, PlatformUserViewModel.class);
    return new ResponseEntity<>(platformUserViewModel, HttpStatus.ACCEPTED);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deletePlatformUserById(@PathVariable final int id) {
    userService.deletePlatformUserById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/drivers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<DriverViewModel> getDriverById(@PathVariable final int id) {
    final GrabDriverDTO grabDriverDTO = userService.getDriverById(id);
    final DriverViewModel driverViewModel = modelMapper.map(grabDriverDTO, DriverViewModel.class);
    return ResponseEntity.ok(driverViewModel);
  }

  @PostMapping(value = "/{id}/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<DriverViewModel> createDriver(
      @PathVariable final int id, @Valid @RequestBody final CreateDriverViewModel createDriverViewModal) {
    final CreateDriverDTO createDriverDTO = modelMapper.map(createDriverViewModal, CreateDriverDTO.class);
    final GrabDriverDTO grabDriverDTO = userService.createDriverById(id, createDriverDTO);
    final DriverViewModel driverViewModel = modelMapper.map(grabDriverDTO, DriverViewModel.class);
    return new ResponseEntity<>(driverViewModel, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/drivers/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deleteDriverById(@PathVariable final int id) {
    userService.deleteDriverById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/passengers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PassengerViewModel> getPassengerById(@PathVariable final int id) {
    final GrabPassengerDTO grabPassengerDTO = userService.getPassengerById(id);
    final PassengerViewModel passengerViewModel = modelMapper.map(grabPassengerDTO, PassengerViewModel.class);
    return ResponseEntity.ok(passengerViewModel);
  }

  @PostMapping(value = "/{id}/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PassengerViewModel> createPassenger(@PathVariable final int id) {
    final GrabPassengerDTO passengerDTO = userService.createPassengerById(id);
    final PassengerViewModel passengerViewModel = modelMapper.map(passengerDTO, PassengerViewModel.class);
    return new ResponseEntity<>(passengerViewModel, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/passengers/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deletePassengerById(@PathVariable final int id) {
    userService.deletePassengerById(id);
    return ResponseEntity.noContent().build();
  }
}
