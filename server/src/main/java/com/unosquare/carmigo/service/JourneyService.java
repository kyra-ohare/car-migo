package com.unosquare.carmigo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabJourneyDriverDTO;
import com.unosquare.carmigo.dto.GrabJourneyPassengerDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Journey;
import com.unosquare.carmigo.entity.Location;
import com.unosquare.carmigo.exception.PatchException;
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.repository.JourneyRepository;
import com.unosquare.carmigo.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JourneyService
{
    private final JourneyRepository journeyRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    public GrabJourneyDriverDTO getJourneyById(final int id)
    {
        return modelMapper.map(findJourneyById(id), GrabJourneyDriverDTO.class);
    }

    public List<GrabJourneyDriverDTO> getJourneys()
    {
        final List<Journey> result = journeyRepository.findAll();
        return MapperUtils.mapList(result, GrabJourneyDriverDTO.class, modelMapper);
    }

    public List<GrabJourneyPassengerDTO> getJourneysByDriverId(final int id)
    {
        final List<Journey> result = journeyRepository.findJourneysByDriverId(id);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No journeys found for driver id %d.", id));
        }
        return MapperUtils.mapList(result, GrabJourneyPassengerDTO.class, modelMapper);
    }

    public List<GrabJourneyDriverDTO> getJourneysByPassengersId(final int id)
    {
        final List<Journey> result = journeyRepository.findJourneysByPassengersId(id);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No journeys found for passenger id %d.", id));
        }
        return MapperUtils.mapList(result, GrabJourneyDriverDTO.class, modelMapper);
    }

    public GrabJourneyDriverDTO createJourney(final CreateJourneyDTO createJourneyDTO)
    {
        final Journey journey = modelMapper.map(createJourneyDTO, Journey.class);
        journey.setCreatedDate(Instant.now());
        journey.setLocationFrom(entityManager.getReference(Location.class, createJourneyDTO.getLocationIdFrom()));
        journey.setLocationTo(entityManager.getReference(Location.class, createJourneyDTO.getLocationIdTo()));
        journey.setDriver(entityManager.getReference(Driver.class, createJourneyDTO.getDriverId()));
        return modelMapper.map(journeyRepository.save(journey), GrabJourneyDriverDTO.class);
    }

    public GrabJourneyDriverDTO patchJourney(final int id, final JsonPatch patch)
    {
        final GrabJourneyDriverDTO grabJourneyDriverDTO = modelMapper.map(findJourneyById(id), GrabJourneyDriverDTO.class);
        try {
            final JsonNode journeyNode = patch.apply(objectMapper.convertValue(grabJourneyDriverDTO, JsonNode.class));
            final Journey patchedJourney = objectMapper.treeToValue(journeyNode, Journey.class);
            return modelMapper.map(journeyRepository.save(patchedJourney), GrabJourneyDriverDTO.class);
        } catch (final JsonPatchException | JsonProcessingException ex) {
            throw new PatchException(
                    String.format("It was not possible to patch journey id %d - %s", id, ex.getMessage()));
        }
    }

    public void deleteJourneyById(final int id)
    {
        journeyRepository.deleteById(id);
    }

    private Journey findJourneyById(final int id)
    {
        return journeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Journey id %d not found.", id)));
    }
}
