package com.unosquare.carmigo.controller;

import com.unosquare.carmigo.model.request.CreateJourneyViewModel;
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

import java.util.Map;

@RestController
@RequestMapping("/v1/journeys")
public class JourneyController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String getPassengerJourneys(
            @RequestParam(name = "passenger_id", required = false) final Optional<Integer> passengerId,
            @RequestParam(name = "driver_id", required = false) final Optional<Integer> driverId) {

        if (passengerId.isPresent()) {
            return "Here are all journeys for passenger " + passengerId.get();
        } else if (driverId.isPresent()) {
            return "Here are all journeys for driver " + driverId.get();
        }
        return "All journeys are here";
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String searchJourneys(@RequestBody final CreateJourneyViewModel createJourneyViewModal) {
        return "Here is your search";
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String createJourneys(@RequestBody final CreateJourneyViewModel createJourneyViewModal) {
        return "Journey created";
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteJourneys(@PathVariable final int id) {
        return "Journey " + id + " was deleted";
    }
}
