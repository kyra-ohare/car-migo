package com.unosquare.carmigo.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreatePlatformUserDTO;
import com.unosquare.carmigo.dto.GrabPlatformUserDTO;
import com.unosquare.carmigo.model.request.CreatePlatformUserViewModel;
import com.unosquare.carmigo.model.response.PlatformUserViewModel;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.PlatformUserService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "Platform User Controller")
public class PlatformUserController {

  private final ModelMapper modelMapper;
  private final PlatformUserService platformUserService;
  private final AppUser appUser;

  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PlatformUserViewModel> getCurrentPlatformUserProfile() {
    return ResponseEntity.ok(getPlatformUser(0));
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<PlatformUserViewModel> getPlatformUserById(@PathVariable final int id) {
    return ResponseEntity.ok(getPlatformUser(id));
  }

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PlatformUserViewModel> createPlatformUser(
      @Valid @RequestBody final CreatePlatformUserViewModel createPlatformUserViewModel) {
    final CreatePlatformUserDTO createPlatformUserDTO = modelMapper.map(
        createPlatformUserViewModel, CreatePlatformUserDTO.class);
    final GrabPlatformUserDTO grabPlatformUserDTO = platformUserService.createPlatformUser(createPlatformUserDTO);
    final PlatformUserViewModel platformUserViewModel = modelMapper.map(
        grabPlatformUserDTO, PlatformUserViewModel.class);
    return new ResponseEntity<>(platformUserViewModel, HttpStatus.CREATED);
  }

  @PatchMapping(consumes = "application/json-patch+json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<PlatformUserViewModel> patchCurrentPlatformUser(@RequestBody final JsonPatch patch) {
    return new ResponseEntity<>(patchPlatformUser(0, patch), HttpStatus.ACCEPTED);
  }

  @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<PlatformUserViewModel> patchPlatformUserById(
      @PathVariable final int id, @RequestBody final JsonPatch patch) {
    return new ResponseEntity<>(patchPlatformUser(id, patch), HttpStatus.ACCEPTED);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deleteCurrentPlatformUser() {
    deletePlatformUser(0);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> deletePlatformUserById(@PathVariable final int id) {
    deletePlatformUser(id);
    return ResponseEntity.noContent().build();
  }

  private PlatformUserViewModel getPlatformUser(final int id) {
    final GrabPlatformUserDTO grabPlatformUserDTO = platformUserService.getPlatformUserById(getCurrentId(id));
    return modelMapper.map(grabPlatformUserDTO, PlatformUserViewModel.class);
  }

  private PlatformUserViewModel patchPlatformUser(final int id, final JsonPatch patch) {
    final GrabPlatformUserDTO grabPlatformUserDTO = platformUserService.patchPlatformUserById(getCurrentId(id), patch);
    return modelMapper.map(grabPlatformUserDTO, PlatformUserViewModel.class);
  }

  private void deletePlatformUser(final int id) {
    platformUserService.deletePlatformUserById(getCurrentId(id));
  }

  private int getCurrentId(final int id) {
    return id != 0 ? id : appUser.get().getId();
  }
}
