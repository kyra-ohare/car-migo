package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "Platform User Controller")
public class PlatformUserController {

  private final ModelMapper modelMapper;
  private final PlatformUserService platformUserService;
  private final AppUser appUser;

  @PostMapping(value = "/confirm-email")
  public ResponseEntity<?> confirmEmail(@RequestParam final String email) {
    platformUserService.confirmEmail(email);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<PlatformUserViewModel> getCurrentPlatformUserProfile() {
    return ResponseEntity.ok(getPlatformUser(ALIAS_CURRENT_USER));
  }

  @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('DEV')")
  public ResponseEntity<PlatformUserViewModel> getPlatformUserById(@PathVariable final int userId) {
    return ResponseEntity.ok(getPlatformUser(userId));
  }

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
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
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<PlatformUserViewModel> patchCurrentPlatformUser(@RequestBody final JsonPatch patch) {
    return new ResponseEntity<>(patchPlatformUser(ALIAS_CURRENT_USER, patch), HttpStatus.ACCEPTED);
  }

  @PatchMapping(value = "/{userId}", consumes = "application/json-patch+json")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<PlatformUserViewModel> patchPlatformUserById(
      @PathVariable final int userId, @RequestBody final JsonPatch patch) {
    return new ResponseEntity<>(patchPlatformUser(userId, patch), HttpStatus.ACCEPTED);
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteCurrentPlatformUser() {
    deletePlatformUser(ALIAS_CURRENT_USER);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "/{userId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> deletePlatformUserById(@PathVariable final int userId) {
    deletePlatformUser(userId);
    return ResponseEntity.noContent().build();
  }

//  @Cacheable(value = "platformUser", key = "#userId")
  private PlatformUserViewModel getPlatformUser(final int userId) {
    System.err.println("!!!! Fetching id " + userId + " !!!!");
    final GrabPlatformUserDTO grabPlatformUserDTO = platformUserService.getPlatformUserById(getCurrentId(userId));
    return modelMapper.map(grabPlatformUserDTO, PlatformUserViewModel.class);
  }

  @CachePut(value = "platformUser", key = "#userId")
  private PlatformUserViewModel patchPlatformUser(final int userId, final JsonPatch patch) {
    final GrabPlatformUserDTO grabPlatformUserDTO =
        platformUserService.patchPlatformUserById(getCurrentId(userId), patch);
    return modelMapper.map(grabPlatformUserDTO, PlatformUserViewModel.class);
  }

//  @CacheEvict(value = "platformUser", allEntries = true)
  private void deletePlatformUser(final int userId) {
    platformUserService.deletePlatformUserById(getCurrentId(userId));
  }

  private int getCurrentId(final int userId) {
    return userId == ALIAS_CURRENT_USER ? appUser.get().getId() : userId;
  }
}
