package com.unosquare.carmigo.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateAuthenticationDTO;
import com.unosquare.carmigo.dto.CreatePlatformUserDTO;
import com.unosquare.carmigo.dto.GrabAuthenticationDTO;
import com.unosquare.carmigo.dto.GrabPlatformUserDTO;
import com.unosquare.carmigo.model.request.CreateAuthenticationViewModel;
import com.unosquare.carmigo.model.request.CreateDriverViewModel;
import com.unosquare.carmigo.model.request.CreatePlatformUserViewModel;
import com.unosquare.carmigo.model.response.AuthenticationViewModel;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "User Controller")
public class UserController {

  private final ModelMapper modelMapper;
  private final UserService userService;
  private final UserControllerHelper userControllerHelper;

  @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<AuthenticationViewModel> createAuthenticationToken(
      @Valid @RequestBody final CreateAuthenticationViewModel createAuthenticationViewModel) {
    final CreateAuthenticationDTO createAuthenticationDTO = modelMapper.map(
        createAuthenticationViewModel, CreateAuthenticationDTO.class);
    final GrabAuthenticationDTO grabAuthenticationDTO = userService.createAuthenticationToken(createAuthenticationDTO);
    final AuthenticationViewModel authenticationViewModel = modelMapper.map(
        grabAuthenticationDTO, AuthenticationViewModel.class);
    return new ResponseEntity<>(authenticationViewModel, HttpStatus.CREATED);
  }

  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PlatformUserViewModel> getPlatformUserProfile() {
    return ResponseEntity.ok(userControllerHelper.getPlatformUserById(0));
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PlatformUserViewModel> createPlatformUser(
      @Valid @RequestBody final CreatePlatformUserViewModel createPlatformUserViewModel) {
    final CreatePlatformUserDTO createPlatformUserDTO = modelMapper.map(
        createPlatformUserViewModel, CreatePlatformUserDTO.class);
    final GrabPlatformUserDTO grabPlatformUserDTO = userService.createPlatformUser(createPlatformUserDTO);
    final PlatformUserViewModel platformUserViewModel = modelMapper.map(
        grabPlatformUserDTO, PlatformUserViewModel.class);
    return new ResponseEntity<>(platformUserViewModel, HttpStatus.CREATED);
  }

  @PatchMapping(consumes = "application/json-patch+json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<PlatformUserViewModel> patchPlatformUser(@RequestBody final JsonPatch patch) {
    return new ResponseEntity<>(userControllerHelper.patchPlatformUserById(0, patch), HttpStatus.ACCEPTED);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deletePlatformUser() {
    userControllerHelper.deletePlatformUserById(0);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/drivers/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<DriverViewModel> getDriverProfile() {
    return ResponseEntity.ok(userControllerHelper.getDriverById(0));
  }

  @PostMapping(value = "/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<DriverViewModel> createDriver(
      @Valid @RequestBody final CreateDriverViewModel createDriverViewModal) {
    final DriverViewModel driverViewModel = userControllerHelper.createDriverById(0, createDriverViewModal);
    return new ResponseEntity<>(driverViewModel, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/drivers")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deleteDriver() {
    userControllerHelper.deleteDriverById(0);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/passengers/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PassengerViewModel> getPassengerProfile() {
    return ResponseEntity.ok(userControllerHelper.getPassengerById(0));
  }

  @PostMapping(value = "/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PassengerViewModel> createPassenger() {
    final PassengerViewModel passengerViewModel = userControllerHelper.createPassengerById(0);
    return new ResponseEntity<>(passengerViewModel, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/passengers")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deletePassenger() {
    userControllerHelper.deletePassengerById(0);
    return ResponseEntity.noContent().build();
  }
}
