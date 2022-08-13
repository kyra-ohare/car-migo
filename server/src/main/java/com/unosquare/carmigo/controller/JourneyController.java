package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.constant.AppConstants.ALIAS_CURRENT_USER;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabDistanceDTO;
import com.unosquare.carmigo.dto.GrabJourneyDTO;
import com.unosquare.carmigo.model.request.CreateCalculateDistanceCriteria;
import com.unosquare.carmigo.model.request.CreateJourneyViewModel;
import com.unosquare.carmigo.model.request.CreateSearchJourneysCriteria;
import com.unosquare.carmigo.model.response.DistanceViewModel;
import com.unosquare.carmigo.model.response.JourneyDriverViewModel;
import com.unosquare.carmigo.model.response.JourneyPassengerViewModel;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.service.JourneyService;
import com.unosquare.carmigo.util.MapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
  public ResponseEntity<JourneyDriverViewModel> getJourneyById(@PathVariable final int journeyId) {
    final GrabJourneyDTO grabJourneyDTO = journeyService.getJourneyById(journeyId);
    final JourneyDriverViewModel journeyDriverViewModel = modelMapper.map(grabJourneyDTO, JourneyDriverViewModel.class);
    return ResponseEntity.ok(journeyDriverViewModel);
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<JourneyDriverViewModel>> searchJourneys(
      @Valid final CreateSearchJourneysCriteria createSearchJourneysCriteria) {
    final List<GrabJourneyDTO> grabJourneyDTOList = journeyService.searchJourneys(createSearchJourneysCriteria);
    final List<JourneyDriverViewModel> journeyDriverViewModelList = MapperUtils.mapList(grabJourneyDTOList,
        JourneyDriverViewModel.class, modelMapper);
    return ResponseEntity.ok(journeyDriverViewModelList);
  }

  @GetMapping(value = "/drivers/my-journeys", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<List<JourneyPassengerViewModel>> getJourneysByCurrentDriver() {
    return ResponseEntity.ok(getJourneysByDriver(ALIAS_CURRENT_USER));
  }

  @GetMapping(value = "/drivers/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<JourneyPassengerViewModel>> getJourneysByDriverId(@PathVariable final int driverId) {
    return ResponseEntity.ok(getJourneysByDriver(driverId));
  }

  @GetMapping(value = "/passengers/my-journeys", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('SUSPENDED') or hasAuthority('ADMIN') or hasAuthority('DEV')")
  public ResponseEntity<List<JourneyDriverViewModel>> getJourneysByCurrentPassenger() {
    return ResponseEntity.ok(getJourneysByPassenger(ALIAS_CURRENT_USER));
  }

  @GetMapping(value = "/passengers/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<JourneyDriverViewModel>> getJourneysByPassengerId(@PathVariable final int passengerId) {
    return ResponseEntity.ok(getJourneysByPassenger(passengerId));
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<JourneyDriverViewModel> createJourney(
      @Valid @RequestBody final CreateJourneyViewModel createJourneyViewModel) {
    return new ResponseEntity<>(createJourney(ALIAS_CURRENT_USER, createJourneyViewModel), HttpStatus.CREATED);
  }

  @PostMapping(value = "/drivers/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<JourneyDriverViewModel> createJourneyByDriverId(
      @PathVariable final int driverId, @Valid @RequestBody final CreateJourneyViewModel createJourneyViewModel) {
    return new ResponseEntity<>(createJourney(driverId, createJourneyViewModel), HttpStatus.CREATED);
  }

  @PostMapping(value = "/{journeyId}/add-passenger", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<JourneyDriverViewModel> addCurrentPassengerToJourney(@PathVariable final int journeyId) {
    return ResponseEntity.ok(addPassengerToJourney(journeyId, ALIAS_CURRENT_USER));
  }

  @PostMapping(value = "/{journeyId}/add-passenger/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<JourneyDriverViewModel> addPassengerToThisJourney(
      @PathVariable final int journeyId, @PathVariable final int passengerId) {
    return ResponseEntity.ok(addPassengerToJourney(journeyId, passengerId));
  }

  @PatchMapping(value = "/{journeyId}", consumes = "application/json-patch+json")
  @PreAuthorize("hasAuthority('ACTIVE') or hasAuthority('ADMIN')")
  public ResponseEntity<JourneyDriverViewModel> patchJourney(
      @PathVariable final int journeyId, @Valid @RequestBody final JsonPatch patch) {
    final GrabJourneyDTO grabJourneyDTO = journeyService.patchJourney(journeyId, patch);
    final JourneyDriverViewModel journeyDriverViewModel = modelMapper.map(grabJourneyDTO, JourneyDriverViewModel.class);
    return ResponseEntity.ok(journeyDriverViewModel);
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
    removePassengerFromJourney(journeyId, ALIAS_CURRENT_USER);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(value = "{journeyId}/remove-passenger/{passengerId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> removePassengerFromThisJourney(@PathVariable final int journeyId,
      @PathVariable final int passengerId) {
    removePassengerFromJourney(journeyId, passengerId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/calculateDistance", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DistanceViewModel> calculateDistance(@Valid final CreateCalculateDistanceCriteria criteria) {
    final GrabDistanceDTO grabDistanceDTO = journeyService.calculateDistance(criteria);
    final DistanceViewModel distanceViewModel = modelMapper.map(grabDistanceDTO, DistanceViewModel.class);
    return ResponseEntity.ok(distanceViewModel);
  }

  private List<JourneyPassengerViewModel> getJourneysByDriver(final int id) {
    final List<GrabJourneyDTO> grabJourneyDTOList = journeyService.getJourneysByDriverId(getCurrentUserId(id));
    return MapperUtils.mapList(grabJourneyDTOList, JourneyPassengerViewModel.class, modelMapper);
  }

  private List<JourneyDriverViewModel> getJourneysByPassenger(final int id) {
    final List<GrabJourneyDTO> grabJourneyDTOList = journeyService.getJourneysByPassengersId(getCurrentUserId(id));
    return MapperUtils.mapList(grabJourneyDTOList, JourneyDriverViewModel.class, modelMapper);
  }

  private JourneyDriverViewModel createJourney(
      @PathVariable final int id, @Valid @RequestBody final CreateJourneyViewModel createJourneyViewModel) {
    final CreateJourneyDTO createJourneyDTO = modelMapper.map(createJourneyViewModel, CreateJourneyDTO.class);
    final GrabJourneyDTO grabJourneyDTO = journeyService.createJourney(getCurrentUserId(id), createJourneyDTO);
    return modelMapper.map(grabJourneyDTO, JourneyDriverViewModel.class);
  }

  private JourneyDriverViewModel addPassengerToJourney(final int journeyId, final int passengerId) {
    final GrabJourneyDTO grabJourneyDTO = journeyService.addPassengerToJourney(journeyId,
        getCurrentUserId(passengerId));
    return modelMapper.map(grabJourneyDTO, JourneyDriverViewModel.class);
  }

  private void removePassengerFromJourney(final int journeyId, final int passengerId) {
    journeyService.removePassengerFromJourney(journeyId, getCurrentUserId(passengerId));
  }

  private int getCurrentUserId(final int id) {
    return id == ALIAS_CURRENT_USER ? appUser.get().getId() : id;
  }
}
