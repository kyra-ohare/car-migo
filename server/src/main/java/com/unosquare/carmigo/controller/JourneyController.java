package com.unosquare.carmigo.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabJourneyDTO;
import com.unosquare.carmigo.model.request.CreateJourneyViewModel;
import com.unosquare.carmigo.model.request.CreateSearchJourneysCriteria;
import com.unosquare.carmigo.model.response.JourneyDriverViewModel;
import com.unosquare.carmigo.model.response.JourneyPassengerViewModel;
import com.unosquare.carmigo.service.JourneyService;
import com.unosquare.carmigo.util.MapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/journeys")
@Tag(name = "Journey Controller")
public class JourneyController
{
    private final ModelMapper modelMapper;
    private final JourneyService journeyService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JourneyDriverViewModel> getJourneyById(@PathVariable final int id)
    {
        final GrabJourneyDTO grabJourneyDTO = journeyService.getJourneyById(id);
        final JourneyDriverViewModel journeyDriverViewModel =
                modelMapper.map(grabJourneyDTO, JourneyDriverViewModel.class);
        return ResponseEntity.ok(journeyDriverViewModel);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JourneyDriverViewModel>> searchJourneys(
            @Valid final CreateSearchJourneysCriteria createSearchJourneysCriteria)
    {
        final List<GrabJourneyDTO> grabJourneyDTOList = journeyService.searchJourneys(createSearchJourneysCriteria);
        final List<JourneyDriverViewModel> journeyDriverViewModelList = MapperUtils.mapList(
                grabJourneyDTOList, JourneyDriverViewModel.class, modelMapper);
        return ResponseEntity.ok(journeyDriverViewModelList);
    }

    @GetMapping(value = "/drivers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JourneyPassengerViewModel>> getJourneysByDriverId(@PathVariable final int id)
    {
        final List<GrabJourneyDTO> grabJourneyDTOList = journeyService.getJourneysByDriverId(id);
        final List<JourneyPassengerViewModel> journeyPassengerViewModelList = MapperUtils.mapList(
                grabJourneyDTOList, JourneyPassengerViewModel.class, modelMapper);
        return ResponseEntity.ok(journeyPassengerViewModelList);
    }

    @GetMapping(value = "/passengers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JourneyDriverViewModel>> getJourneysByPassengerId(@PathVariable final int id)
    {
        final List<GrabJourneyDTO> grabJourneyDTOList = journeyService.getJourneysByPassengersId(id);
        final List<JourneyDriverViewModel> journeyDriverViewModelList = MapperUtils.mapList(
                grabJourneyDTOList, JourneyDriverViewModel.class, modelMapper);
        return ResponseEntity.ok(journeyDriverViewModelList);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<JourneyDriverViewModel> createJourney(
            @Valid @RequestBody final CreateJourneyViewModel createJourneyViewModel)
    {
        final CreateJourneyDTO createJourneyDTO = modelMapper.map(createJourneyViewModel, CreateJourneyDTO.class);
        final GrabJourneyDTO grabJourneyDTO = journeyService.createJourney(createJourneyDTO);
        final JourneyDriverViewModel journeyDriverViewModel =
                modelMapper.map(grabJourneyDTO, JourneyDriverViewModel.class);
        return new ResponseEntity<>(journeyDriverViewModel, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JourneyDriverViewModel> patchJourney(@PathVariable final int id,
                                                               @Valid @RequestBody final JsonPatch patch)
    {
        final GrabJourneyDTO grabJourneyDTO = journeyService.patchJourney(id, patch);
        final JourneyDriverViewModel journeyDriverViewModel =
                modelMapper.map(grabJourneyDTO, JourneyDriverViewModel.class);
        return ResponseEntity.ok(journeyDriverViewModel);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteJourney(@PathVariable final int id)
    {
        journeyService.deleteJourneyById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "{journeyId}/passengers/{passengerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deletePassengerJourney(@PathVariable final int journeyId,
                                                    @PathVariable final int passengerId)
    {
        journeyService.deleteByJourneyIdAndPassengerId(journeyId, passengerId);
        return ResponseEntity.noContent().build();
    }
}
