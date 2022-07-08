package com.unosquare.carmigo.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.CreatePlatformUserDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.dto.GrabPlatformUserDTO;
import com.unosquare.carmigo.model.request.CreateDriverViewModel;
import com.unosquare.carmigo.model.request.CreatePlatformUserViewModel;
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

  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PlatformUserViewModel> getPlatformUserProfile() {
    final GrabPlatformUserDTO grabPlatformUserDTO = userService.getPlatformUserById(0);
    final PlatformUserViewModel platformUserViewModel = modelMapper.map(
        grabPlatformUserDTO, PlatformUserViewModel.class);
    return ResponseEntity.ok(platformUserViewModel);
  }

  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
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
    final GrabPlatformUserDTO grabPlatformUserDTO = userService.patchPlatformUserById(0, patch);
    final PlatformUserViewModel platformUserViewModel = modelMapper.map(
        grabPlatformUserDTO, PlatformUserViewModel.class);
    return new ResponseEntity<>(platformUserViewModel, HttpStatus.ACCEPTED);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deletePlatformUser() {
    userService.deletePlatformUserById(0);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/drivers/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<DriverViewModel> getDriverProfile() {
    final GrabDriverDTO grabDriverDTO = userService.getDriverById(0);
    final DriverViewModel driverViewModel = modelMapper.map(grabDriverDTO, DriverViewModel.class);
    return ResponseEntity.ok(driverViewModel);
  }

  @PostMapping(value = "/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<DriverViewModel> createDriver(
      @Valid @RequestBody final CreateDriverViewModel createDriverViewModal) {
    final CreateDriverDTO createDriverDTO = modelMapper.map(createDriverViewModal, CreateDriverDTO.class);
    final GrabDriverDTO grabDriverDTO = userService.createDriverById(0, createDriverDTO);
    final DriverViewModel driverViewModel = modelMapper.map(grabDriverDTO, DriverViewModel.class);
    return new ResponseEntity<>(driverViewModel, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/drivers")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deleteDriver() {
    userService.deleteDriverById(0);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/passengers/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PassengerViewModel> getPassengerProfile() {
    final GrabPassengerDTO grabPassengerDTO = userService.getPassengerById(0);
    final PassengerViewModel passengerViewModel = modelMapper.map(grabPassengerDTO, PassengerViewModel.class);
    return ResponseEntity.ok(passengerViewModel);
  }

  @PostMapping(value = "/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PassengerViewModel> createPassenger() {
    final GrabPassengerDTO passengerDTO = userService.createPassengerById(0);
    final PassengerViewModel passengerViewModel = modelMapper.map(passengerDTO, PassengerViewModel.class);
    return new ResponseEntity<>(passengerViewModel, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/passengers")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deletePassenger() {
    userService.deletePassengerById(0);
    return ResponseEntity.noContent().build();
  }
}
