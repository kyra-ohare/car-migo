package com.unosquare.carmigo.service;

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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JourneyService
{
    private final JourneyRepository journeyRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    public GrabJourneyDTO getJourneyById(final int id)
    {
        final Journey journey = journeyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journey id " + id + " not found."));
        return modelMapper.map(journey, GrabJourneyDTO.class);
    }

    public List<GrabJourneyDTO> getJourneyParameters(final Map<String, String> paramMap)
    {
        if (!paramMap.isEmpty()) {
            final Map.Entry<String, String> entry = paramMap.entrySet().iterator().next();
            final String key = entry.getKey();
            final int value;
            try {
                value = Integer.parseInt(entry.getValue());
            } catch (final NumberFormatException ex) {
                throw new ResourceNotFoundException("ID is not a number.");
            }
            switch (key) {
                case "passenger_id":
                    return null;
                case "driver_id":
                    final List<Journey> result = journeyRepository.findJourneyByDriverId(value);
                    return MapperUtils.mapList(result, GrabJourneyDTO.class, modelMapper);
                default:
                    throw new ResourceNotFoundException("Resource not found.");
            }
        } else {
            final List<Journey> result = journeyRepository.findAll();
            return MapperUtils.mapList(result, GrabJourneyDTO.class, modelMapper);
        }
    }

    public GrabJourneyDTO createJourney(final CreateJourneyDTO createJourneyDTO)
    {
        final Journey journey = modelMapper.map(createJourneyDTO, Journey.class);
        journey.setCreatedDate(Instant.now());
        journey.setLocationIdFrom(entityManager.getReference(Location.class, createJourneyDTO.getLocationIdFrom()));
        journey.setLocationIdTo(entityManager.getReference(Location.class, createJourneyDTO.getLocationIdTo()));
        journey.setDriver(entityManager.getReference(Driver.class, createJourneyDTO.getDriver()));
        return modelMapper.map(journeyRepository.save(journey), GrabJourneyDTO.class);
    }

    public void deleteJourneyById(final int id)
    {
        journeyRepository.deleteById(id);
    }
}
