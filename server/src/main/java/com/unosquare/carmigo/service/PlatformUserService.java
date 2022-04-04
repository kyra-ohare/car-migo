package com.unosquare.carmigo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.CreatePlatformUserDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.dto.GrabPlatformUserDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.entity.UserAccessStatus;
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.repository.DriverRepository;
import com.unosquare.carmigo.repository.PassengerRepository;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PlatformUserService
{
    private static final int INITIAL_USER_STATUS = 1;

    private final PlatformUserRepository platformUserRepository;
    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;


    public GrabPlatformUserDTO getPlatformUserById(final int id)
    {
        final PlatformUser platformUser = findPlatformUserById(id);
        return modelMapper.map(platformUser, GrabPlatformUserDTO.class);
    }

    public GrabPlatformUserDTO createPlatformUser(final CreatePlatformUserDTO createPlatformUserDTO)
    {
        final PlatformUser platformUser = modelMapper.map(createPlatformUserDTO, PlatformUser.class);
        platformUser.setCreatedDate(Instant.now());
        platformUser.setUserAccessStatus(entityManager.getReference(UserAccessStatus.class, INITIAL_USER_STATUS));
        return modelMapper.map(platformUserRepository.save(platformUser), GrabPlatformUserDTO.class);
    }

    public GrabPlatformUserDTO patchPlatformUser(final int id, final JsonPatch patch)
    {
        final GrabPlatformUserDTO grabPlatformUserDTO = modelMapper.map(
                findPlatformUserById(id), GrabPlatformUserDTO.class);
        try {
            final JsonNode platformUserNode = patch.apply(
                    objectMapper.convertValue(grabPlatformUserDTO, JsonNode.class));
            final PlatformUser patchedPlatformUser = objectMapper.treeToValue(platformUserNode, PlatformUser.class);
            return modelMapper.map(platformUserRepository.save(patchedPlatformUser), GrabPlatformUserDTO.class);
        } catch (final JsonPatchException | JsonProcessingException ex) {
            throw new ResourceNotFoundException("Error updating user id " + id);
        }
    }

    public void deletePlatformUserById(final int id)
    {
        platformUserRepository.deleteById(id);
    }

    public GrabDriverDTO getDriverById(final int id)
    {
        final Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Driver id %d not found.", id)));
        return modelMapper.map(driver, GrabDriverDTO.class);
    }

    public GrabDriverDTO createDriver(final int id, final CreateDriverDTO createDriverDTO)
    {
        final Driver driver = modelMapper.map(createDriverDTO, Driver.class);
        driver.setPlatformUser(entityManager.getReference(PlatformUser.class, id));
        return modelMapper.map(driverRepository.save(driver), GrabDriverDTO.class);
    }

    public void deleteDriverById(final int id)
    {
        driverRepository.deleteById(id);
    }

    public GrabPassengerDTO getPassengerById(final int id)
    {
        final Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger id " + id + " not found."));
        return modelMapper.map(passenger, GrabPassengerDTO.class);
    }

    public GrabPassengerDTO createPassenger(final int id)
    {
        final Passenger passenger = new Passenger();
        passenger.setPlatformUser(entityManager.getReference(PlatformUser.class, id));
        return modelMapper.map(passengerRepository.save(passenger), GrabPassengerDTO.class);
    }

    public void deletePassengerById(final int id)
    {
        passengerRepository.deleteById(id);
    }

    private PlatformUser findPlatformUserById(final int id)
    {
        return platformUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("PlatformUser id %d not found.", id)));
    }
}
