package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabDistanceDTO;
import com.unosquare.carmigo.dto.GrabJourneyDTO;
import com.unosquare.carmigo.model.request.CalculateDistanceCriteriaRequest;
import com.unosquare.carmigo.model.request.JourneyRequest;
import com.unosquare.carmigo.model.request.SearchJourneysRequest;
import com.unosquare.carmigo.model.response.DistanceResponse;
import com.unosquare.carmigo.model.response.JourneyResponse;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.JourneyService;
import com.unosquare.carmigo.util.MapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/journeys")
@Tag(name = "Journey Controller")
public class JourneyController {

  private final ModelMapper modelMapper;
  private final JourneyService journeyService;
  private final AppUser appUser;

  @GetMapping(value = "/{journeyId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> getJourneyById(@PathVariable final int journeyId) {
    final GrabJourneyDTO grabJourneyDTO = journeyService.getJourneyById(journeyId);
    final JourneyResponse journeyResponse = modelMapper.map(grabJourneyDTO, JourneyResponse.class);
    return ResponseEntity.ok(journeyResponse);
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<JourneyResponse>> searchJourneys(
      @Valid final SearchJourneysRequest searchJourneysRequest) {
    final List<GrabJourneyDTO> grabJourneyDTOList = journeyService.searchJourneys(searchJourneysRequest);
    final List<JourneyResponse> journeyResponseList = MapperUtils.mapList(grabJourneyDTOList,
        JourneyResponse.class, modelMapper);
    return ResponseEntity.ok(journeyResponseList);
  }

  @GetMapping(value = "/drivers/my-journeys", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByCurrentDriver() {
    return ResponseEntity.ok(getJourneysByDriver(ALIAS_CURRENT_USER));
  }

  @GetMapping(value = "/drivers/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByDriverId(@PathVariable final int driverId) {
    return ResponseEntity.ok(getJourneysByDriver(driverId));
  }

  @GetMapping(value = "/passengers/my-journeys", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByCurrentPassenger() {
    return ResponseEntity.ok(getJourneysByPassenger(ALIAS_CURRENT_USER));
  }

  @GetMapping(value = "/passengers/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<JourneyResponse>> getJourneysByPassengerId(@PathVariable final int passengerId) {
    return ResponseEntity.ok(getJourneysByPassenger(passengerId));
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> createJourney(
      @Valid @RequestBody final JourneyRequest journeyRequest) {
    return new ResponseEntity<>(createJourney(ALIAS_CURRENT_USER, journeyRequest), HttpStatus.CREATED);
  }

  @PostMapping(value = "/drivers/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<JourneyResponse> createJourneyByDriverId(
      @PathVariable final int driverId, @Valid @RequestBody final JourneyRequest journeyRequest) {
    return new ResponseEntity<>(createJourney(driverId, journeyRequest), HttpStatus.CREATED);
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
    final GrabJourneyDTO grabJourneyDTO = journeyService.patchJourney(journeyId, patch);
    final JourneyResponse journeyResponse = modelMapper.map(grabJourneyDTO, JourneyResponse.class);
    return ResponseEntity.ok(journeyResponse);
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
  public ResponseEntity<DistanceResponse> calculateDistance(@Valid final CalculateDistanceCriteriaRequest criteria) {
    final GrabDistanceDTO grabDistanceDTO = journeyService.calculateDistance(criteria);
    final DistanceResponse distanceResponse = modelMapper.map(grabDistanceDTO, DistanceResponse.class);
    return ResponseEntity.ok(distanceResponse);
  }

  private List<JourneyResponse> getJourneysByDriver(final int driverId) {
    final List<GrabJourneyDTO> grabJourneyDTOList = journeyService.getJourneysByDriverId(getCurrentUserId(driverId));
    return MapperUtils.mapList(grabJourneyDTOList, JourneyResponse.class, modelMapper);
  }

  private List<JourneyResponse> getJourneysByPassenger(final int passengerId) {
    final List<GrabJourneyDTO> grabJourneyDTOList = journeyService.getJourneysByPassengersId(
        getCurrentUserId(passengerId));
    return MapperUtils.mapList(grabJourneyDTOList, JourneyResponse.class, modelMapper);
  }

  private JourneyResponse createJourney(final int driverId, final JourneyRequest journeyRequest) {
    final CreateJourneyDTO createJourneyDTO = modelMapper.map(journeyRequest, CreateJourneyDTO.class);
    final GrabJourneyDTO grabJourneyDTO = journeyService.createJourney(getCurrentUserId(driverId), createJourneyDTO);
    return modelMapper.map(grabJourneyDTO, JourneyResponse.class);
  }

  private int getCurrentUserId(final int id) {
    return id == ALIAS_CURRENT_USER ? appUser.get().getId() : id;
  }
}
