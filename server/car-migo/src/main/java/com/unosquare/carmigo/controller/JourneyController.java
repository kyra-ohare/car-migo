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

/**
 * Handles Journey APIs.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/journeys")
@Tag(name = "Journey Controller")
public class JourneyController {

  private final JourneyService journeyService;
  private final AppUser appUser;

  /**
   * Enables logged-in admin users to search for a specific journey.
   *
   * @param journeyId the journey's id.
   * @return Response body as {@link JourneyResponse}.
   */
  @GetMapping(value = "/{journeyId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> getJourneyById(@PathVariable final int journeyId) {
    final var response = journeyService.getJourneyById(journeyId);
    return ResponseEntity.ok(response);
  }

  /**
   * Enables users to search for journeys. No need of authentication.
   *
   * @param searchJourneysRequest Request body as {@link SearchJourneysRequest}.
   * @return Response body as List of {@link JourneyResponse}.
   */
  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<JourneyResponse>> searchJourneys(
      @Valid final SearchJourneysRequest searchJourneysRequest) {
    final var response = journeyService.searchJourneys(searchJourneysRequest);
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in user, as a driver, to search for their journeys.
   *
   * @return Response body as List of {@link JourneyResponse}.
   */
  @GetMapping(value = "/drivers/my-journeys", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByCurrentDriver() {
    final var response = journeyService.getJourneysByDriverId(getCurrentUserId(ALIAS_CURRENT_USER));
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in admin users to search for journeys of a driver.
   *
   * @param driverId the driver's id.
   * @return Response body as List of {@link JourneyResponse}.
   */
  @GetMapping(value = "/drivers/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByDriverId(@PathVariable final int driverId) {
    final var response = journeyService.getJourneysByDriverId(getCurrentUserId(driverId));
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in user, as a passenger, to search their journeys.
   *
   * @return Response body as List of {@link JourneyResponse}.
   */
  @GetMapping(value = "/passengers/my-journeys", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByCurrentPassenger() {
    final var response = journeyService.getJourneysByPassengersId(getCurrentUserId(ALIAS_CURRENT_USER));
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in admin users to search for journeys of a passenger.
   *
   * @param passengerId the passenger's id.
   * @return Response body as List of {@link JourneyResponse}.
   */
  @GetMapping(value = "/passengers/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByPassengerId(@PathVariable final int passengerId) {
    final var response = journeyService.getJourneysByPassengersId(getCurrentUserId(passengerId));
    return ResponseEntity.ok(response);
  }

  /**
   * Enables logged-in user, as a driver, to create a journey.
   *
   * @param journeyRequest Request body as {@link JourneyRequest}.
   * @return Response body as {@link JourneyResponse}.
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> createJourney(
      @Valid @RequestBody final JourneyRequest journeyRequest) {
    final var response = journeyService.createJourney(getCurrentUserId(ALIAS_CURRENT_USER), journeyRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Enables logged-in admin users to create a journey for a driver.
   *
   * @param driverId       the driver id to create a journey for.
   * @param journeyRequest Request body as {@link JourneyRequest}.
   * @return Response body as {@link JourneyResponse}.
   */
  @PostMapping(value = "/drivers/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> createJourneyByDriverId(
      @PathVariable final int driverId, @Valid @RequestBody final JourneyRequest journeyRequest) {
    final var response = journeyService.createJourney(getCurrentUserId(driverId), journeyRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Enables logged-in user, as a passenger, to be a passenger of this journey.
   *
   * @param journeyId the journey id to add this passenger.
   * @return an empty body.
   */
  @PostMapping(value = "/{journeyId}/add-passenger", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> addCurrentPassengerToJourney(@PathVariable final int journeyId) {
    journeyService.addPassengerToJourney(journeyId, appUser.get().getId());
    return ResponseEntity.ok().build();
  }

  /**
   * Enables logged-in admin users to add this passenger to this journey.
   *
   * @param journeyId   the journey id to add this passenger.
   * @param passengerId the passenger id to be added to this journey.
   * @return an empty body.
   */
  @PostMapping(value = "/{journeyId}/add-passenger/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> addPassengerToThisJourney(
      @PathVariable final int journeyId, @PathVariable final int passengerId) {
    journeyService.addPassengerToJourney(journeyId, passengerId);
    return ResponseEntity.ok().build();
  }

  /**
   * Enables logged-in admin users or the driver who owns this journey to make correction.
   *
   * @param journeyId the journey id to be corrected.
   * @param patch     Request body as {@link JsonPatch}.
   * @return Response body as {@link JourneyResponse}.
   */
  @PatchMapping(value = "/{journeyId}", consumes = "application/json-patch+json")
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> patchJourney(
      @PathVariable final int journeyId, @Valid @RequestBody final JsonPatch patch) {
    final var response = journeyService.patchJourney(journeyId, patch);
    return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
  }

  /**
   * Enables logged-in admin users or the driver who owns this journey to delete.
   *
   * @param journeyId the journey id to be deleted.
   * @return an empty body.
   */
  @DeleteMapping(value = "/{journeyId}")
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteJourney(@PathVariable final int journeyId) {
    journeyService.deleteJourneyById(journeyId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Enables a user, as a passenger, to no longer be part of this journey.
   *
   * @param journeyId the journey id to remove a passenger.
   * @return an empty body.
   */
  @DeleteMapping(value = "{journeyId}/remove-passenger")
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<?> removeCurrentPassengerFromJourney(@PathVariable final int journeyId) {
    journeyService.removePassengerFromJourney(journeyId, appUser.get().getId());
    return ResponseEntity.noContent().build();
  }

  /**
   * Enables logged-in admin users to remove a passenger from a journey.
   *
   * @param journeyId   the journey id to remove a passenger.
   * @param passengerId the passenger id to be removed from a journey.
   * @return an empty body.
   */
  @DeleteMapping(value = "{journeyId}/remove-passenger/{passengerId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> removePassengerFromThisJourney(@PathVariable final int journeyId,
      @PathVariable final int passengerId) {
    journeyService.removePassengerFromJourney(journeyId, passengerId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Enables users to search for the distance of two points. No need of authentication.
   *
   * @param distanceRequest the search criteria as {@link DistanceRequest}.
   * @return Response body as {@link DistanceResponse}.
   */
  @GetMapping(value = "/calculateDistance", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DistanceResponse> calculateDistance(@Valid final DistanceRequest distanceRequest) {
    final var response = journeyService.calculateDistance(distanceRequest);
    return ResponseEntity.ok(response);
  }

  private int getCurrentUserId(final int id) {
    return id == ALIAS_CURRENT_USER ? appUser.get().getId() : id;
  }
}
