package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.request.DistanceRequest;
import com.unosquare.carmigo.dto.request.JourneyRequest;
import com.unosquare.carmigo.dto.request.SearchJourneysRequest;
import com.unosquare.carmigo.dto.response.DistanceResponse;
import com.unosquare.carmigo.dto.response.JourneyResponse;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.JourneyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/journeys")
@Tag(name = "Journey Controller")
public class JourneyController {

  private final JourneyService journeyService;
  private final AppUser appUser;

  @GetMapping(value = "/{journeyId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> getJourneyById(@PathVariable final int journeyId) {
    final var response = journeyService.getJourneyById(journeyId);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<JourneyResponse>> searchJourneys(
    @Valid final SearchJourneysRequest searchJourneysRequest) {
    final List<JourneyResponse> responses = journeyService.searchJourneys(searchJourneysRequest);
    return ResponseEntity.ok(responses);
  }

  @GetMapping(value = "/drivers/my-journeys", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByCurrentDriver() {
    final var response = journeyService.getJourneysByDriverId(getCurrentUserId(ALIAS_CURRENT_USER));
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/drivers/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByDriverId(@PathVariable final int driverId) {
    final var response = journeyService.getJourneysByDriverId(getCurrentUserId(driverId));
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/passengers/my-journeys", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByCurrentPassenger() {
    final var response = journeyService.getJourneysByPassengersId(getCurrentUserId(ALIAS_CURRENT_USER));
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/passengers/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByPassengerId(@PathVariable final int passengerId) {
    final var response = journeyService.getJourneysByPassengersId(getCurrentUserId(passengerId));
    return ResponseEntity.ok(response);
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> createJourney(
    @Valid @RequestBody final JourneyRequest journeyRequest) {
    final var response = journeyService.createJourney(getCurrentUserId(ALIAS_CURRENT_USER), journeyRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping(value = "/drivers/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> createJourneyByDriverId(
    @PathVariable final int driverId, @Valid @RequestBody final JourneyRequest journeyRequest) {
    final var response = journeyService.createJourney(getCurrentUserId(driverId), journeyRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping(value = "/{journeyId}/add-passenger", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> addCurrentPassengerToJourney(@PathVariable final int journeyId) {
    journeyService.addPassengerToJourney(journeyId, appUser.get().getId());
    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/{journeyId}/add-passenger/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> addPassengerToThisJourney(
    @PathVariable final int journeyId, @PathVariable final int passengerId) {
    journeyService.addPassengerToJourney(journeyId, passengerId);
    return ResponseEntity.ok().build();
  }

  @PatchMapping(value = "/{journeyId}", consumes = "application/json-patch+json")
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> patchJourney(
    @PathVariable final int journeyId, @Valid @RequestBody final JsonPatch patch) {
    final var response = journeyService.patchJourney(journeyId, patch);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping(value = "/{journeyId}")
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteJourney(@PathVariable final int journeyId) {
    journeyService.deleteJourneyById(journeyId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(value = "{journeyId}/remove-passenger")
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> removeCurrentPassengerFromJourney(@PathVariable final int journeyId) {
    journeyService.removePassengerFromJourney(journeyId, appUser.get().getId());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(value = "{journeyId}/remove-passenger/{passengerId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> removePassengerFromThisJourney(@PathVariable final int journeyId,
    @PathVariable final int passengerId) {
    journeyService.removePassengerFromJourney(journeyId, passengerId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/calculateDistance", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DistanceResponse> calculateDistance(@Valid final DistanceRequest criteria) {
    final var response = journeyService.calculateDistance(criteria);
    return ResponseEntity.ok(response);
  }

  private int getCurrentUserId(final int id) {
    return id == ALIAS_CURRENT_USER ? appUser.get().getId() : id;
  }
}
