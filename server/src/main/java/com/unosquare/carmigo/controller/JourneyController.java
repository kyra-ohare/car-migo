package com.unosquare.carmigo.controller;

import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabJourneyDTO;
import com.unosquare.carmigo.model.request.CreateJourneyViewModel;
import com.unosquare.carmigo.model.response.JourneyViewModel;
import com.unosquare.carmigo.service.JourneyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/journeys")
public class JourneyController
{
    final ModelMapper modelMapper;
    final JourneyService journeyService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    //    public ResponseEntity<String>
    public String getJourneys(@RequestParam final Map<String, String> paramMap)
    {
        return JourneyService.getJourneyParameters(paramMap);

        // response with a response model (JourneyViewModel)

        //        return ResponseEntity.ok("OK");
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String searchJourneys(@RequestBody final CreateJourneyViewModel createJourneyViewModal)
    {
        return "Here is your search";
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<JourneyViewModel> createJourney(
            @RequestBody final CreateJourneyViewModel createJourneyViewModal)
    {
        final CreateJourneyDTO createJourneyDTO = modelMapper.map(
                createJourneyViewModal, CreateJourneyDTO.class);
        final GrabJourneyDTO grabJourneyDTO = journeyService.createJourney(createJourneyDTO);
        final JourneyViewModel journeyViewModel = modelMapper.map(
                grabJourneyDTO, JourneyViewModel.class);
        return new ResponseEntity<>(journeyViewModel, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteJourney(@PathVariable final int id)
    {
        journeyService.deleteJourneyById(id);
        return ResponseEntity.noContent().build();
    }
}
