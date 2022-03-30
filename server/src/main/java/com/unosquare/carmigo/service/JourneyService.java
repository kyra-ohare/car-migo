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
import com.unosquare.carmigo.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
        final Journey journey = journeyRepository.findJourneyById(id);

        if (journey == null) {
            throw new ResourceNotFoundException(String.format("Journey id %d not found.", id));
        }
        return modelMapper.map(journey, GrabJourneyDTO.class);
    }

    public List<GrabJourneyDTO> getJourneys()
    {
        final List<Journey> result = journeyRepository.findAll();
        return MapperUtils.mapList(result, GrabJourneyDTO.class, modelMapper);
    }

    public GrabJourneyDTO createJourney(final CreateJourneyDTO createJourneyDTO)
    {
        final Journey journey = modelMapper.map(createJourneyDTO, Journey.class);
        journey.setCreatedDate(Instant.now());
        journey.setLocationFrom(entityManager.getReference(Location.class, createJourneyDTO.getLocationIdFrom()));
        journey.setLocationTo(entityManager.getReference(Location.class, createJourneyDTO.getLocationIdTo()));
        journey.setDriver(entityManager.getReference(Driver.class, createJourneyDTO.getDriverId()));
        return modelMapper.map(journeyRepository.save(journey), GrabJourneyDTO.class);
    }

    public GrabJourneyDTO patchJourney(final int journeyId, final int driverId, final JsonPatch patch)
    {
        final List<Journey> journeys = journeyRepository.findJourneyByDriverId(driverId);
        final Optional<Journey> targetJourney = journeys.stream()
                .filter(journey -> journey.getId() == journeyId)
                .findFirst();
        final String errorMsg = String.format("Journey id %d whose driver's id is %d not found.", journeyId, driverId);
        if(targetJourney.isEmpty()) {
            throw new ResourceNotFoundException(errorMsg);
        }
        try {
            final GrabJourneyDTO grabJourneyDTO = modelMapper.map(targetJourney.get(), GrabJourneyDTO.class);
            final JsonNode journeyNode = patch.apply(objectMapper.convertValue(grabJourneyDTO, JsonNode.class));
            final Journey patchedJourney = objectMapper.treeToValue(journeyNode, Journey.class);
            return modelMapper.map(journeyRepository.save(patchedJourney), GrabJourneyDTO.class);
        } catch (final JsonPatchException | JsonProcessingException ex) {
            throw new ResourceNotFoundException(errorMsg);
        }
    }

    public void deleteJourneyById(final int id)
    {
        journeyRepository.deleteById(id);
    }
}
