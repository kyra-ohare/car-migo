package com.unosquare.carmigo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabJourneyDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Journey;
import com.unosquare.carmigo.entity.Location;
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.repository.JourneyRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class JourneyService
{
    private final JourneyRepository journeyRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    public GrabJourneyDTO getJourneyById(final int id)
    {
        final Journey journey = journeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journey id " + id + " not found."));
        return modelMapper.map(journey, GrabJourneyDTO.class);
    }

    public GrabJourneyDTO createJourney(final CreateJourneyDTO createJourneyDTO)
    {
        final Journey journey = modelMapper.map(createJourneyDTO, Journey.class);
        journey.setCreatedDate(Instant.now());
        journey.setLocationFrom(entityManager.getReference(Location.class, createJourneyDTO.getLocationIdFrom()));
        journey.setLocationTo(entityManager.getReference(Location.class, createJourneyDTO.getLocationIdTo()));
        journey.setDriver(entityManager.getReference(Driver.class, createJourneyDTO.getDriver()));
        return modelMapper.map(journeyRepository.save(journey), GrabJourneyDTO.class);
    }

    public GrabJourneyDTO patchJourney(int journeyId, int driverId, JsonPatch patch)
    {
        final List<Journey> journeys = journeyRepository.findJourneyByDriverId(driverId);
        final Optional<Journey> targetJourney = journeys.stream()
            .filter(journey -> journey.getId() == journeyId).findFirst();
        try {
            if (targetJourney.isPresent()) {
                final GrabJourneyDTO grabJourneyDTO = modelMapper.map(targetJourney.get(), GrabJourneyDTO.class);
                final JsonNode journeyNode = patch.apply(objectMapper.convertValue(grabJourneyDTO, JsonNode.class));
                final Journey patchedJourney = objectMapper.treeToValue(journeyNode, Journey.class);
                return modelMapper.map(journeyRepository.save(patchedJourney), GrabJourneyDTO.class);
            }
            throw new JsonPatchException("targetJourney is empty");
        } catch (final JsonPatchException | JsonProcessingException ex) {
            throw new ResourceNotFoundException("Error updating Journey id " + journeyId + " whose driver's id is " + driverId);
        }
    }

    public void deleteJourneyById(final int id)
    {
        journeyRepository.deleteById(id);
    }
}
