package com.unosquare.carmigo.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabJourneyDriverDTO;
import com.unosquare.carmigo.dto.GrabJourneyPassengerDTO;
import com.unosquare.carmigo.model.request.CreateJourneyViewModel;
import com.unosquare.carmigo.model.response.JourneyDriverViewModel;
import com.unosquare.carmigo.model.response.JourneyPassengerViewModel;
import com.unosquare.carmigo.service.JourneyService;
import com.unosquare.carmigo.util.MapperUtils;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/journeys")
public class JourneyController
{
    private final ModelMapper modelMapper;
    private final JourneyService journeyService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JourneyDriverViewModel> getJourneyById(@PathVariable final int id)
    {
        final GrabJourneyDriverDTO grabJourneyDriverDTO = journeyService.getJourneyById(id);
        final JourneyDriverViewModel journeyDriverViewModel = modelMapper.map(grabJourneyDriverDTO, JourneyDriverViewModel.class);
        return ResponseEntity.ok(journeyDriverViewModel);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JourneyDriverViewModel>> getJourneys()
    {
        final List<GrabJourneyDriverDTO> grabJourneyDriverDTOList = journeyService.getJourneys();
        final List<JourneyDriverViewModel> journeyDriverViewModelList = MapperUtils.mapList(
                grabJourneyDriverDTOList, JourneyDriverViewModel.class, modelMapper);
        return ResponseEntity.ok(journeyDriverViewModelList);
    }

    @GetMapping(value = "/driver/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JourneyPassengerViewModel>> getJourneysByDriverId(@PathVariable final int id)
    {
        final List<GrabJourneyPassengerDTO> grabJourneyPassengerDTOList = journeyService.getJourneysByDriverId(id);
        final List<JourneyPassengerViewModel> journeyPassengerViewModelList = MapperUtils.mapList(
                grabJourneyPassengerDTOList, JourneyPassengerViewModel.class, modelMapper);
        return ResponseEntity.ok(journeyPassengerViewModelList);
    }

    @GetMapping(value = "/passenger/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JourneyDriverViewModel>> getJourneysByPassengerId(@PathVariable final int id)
    {
        final List<GrabJourneyDriverDTO> grabJourneyDriverDTOList = journeyService.getJourneysByPassengersId(id);
        final List<JourneyDriverViewModel> journeyDriverViewModelList = MapperUtils.mapList(
                grabJourneyDriverDTOList, JourneyDriverViewModel.class, modelMapper);
        return ResponseEntity.ok(journeyDriverViewModelList);
    }

    // todo
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String searchJourneys(@RequestBody final CreateJourneyViewModel createJourneyViewModal)
    {
        return "Here is your search";
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<JourneyDriverViewModel> createJourney(
            @Valid @RequestBody final CreateJourneyViewModel createJourneyViewModal)
    {
        final CreateJourneyDTO createJourneyDTO = modelMapper.map(createJourneyViewModal, CreateJourneyDTO.class);
        final GrabJourneyDriverDTO grabJourneyDriverDTO = journeyService.createJourney(createJourneyDTO);
        final JourneyDriverViewModel journeyDriverViewModel = modelMapper.map(grabJourneyDriverDTO, JourneyDriverViewModel.class);
        return new ResponseEntity<>(journeyDriverViewModel, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<JourneyDriverViewModel> patchJourney(@PathVariable final int id,
                                                               @Valid @RequestBody final JsonPatch patch)
    {
        final GrabJourneyDriverDTO grabJourneyDriverDTO = journeyService.patchJourney(id, patch);
        final JourneyDriverViewModel journeyDriverViewModel = modelMapper.map(grabJourneyDriverDTO, JourneyDriverViewModel.class);
        return ResponseEntity.ok(journeyDriverViewModel);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteJourney(@PathVariable final int id)
    {
        journeyService.deleteJourneyById(id);
        return ResponseEntity.noContent().build();
    }
}
