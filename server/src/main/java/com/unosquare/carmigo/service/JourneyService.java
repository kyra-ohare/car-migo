package com.unosquare.carmigo.service;

import com.unosquare.carmigo.dto.CreateJourneyDTO;
import com.unosquare.carmigo.dto.GrabJourneyDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Journey;
import com.unosquare.carmigo.entity.Location;
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.repository.JourneyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.Instant;
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

    {
        if (!paramMap.isEmpty()) {
            final Map.Entry<String, String> entry = paramMap.entrySet().iterator().next();
            final String key = entry.getKey();
            final String value = entry.getValue();

            switch (key) {
                case "passenger_id":
                    return "Here are all journeys for passenger " + value;
                case "driver_id":
                    return "Here are all journeys for driver " + value;
                default:
                    return "Error retrieving journeys";
            }
        } else {
            return "Here are all your journeys";
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
