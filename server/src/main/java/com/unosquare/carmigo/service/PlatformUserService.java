package com.unosquare.carmigo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.unosquare.carmigo.dto.CreateAuthenticationDTO;
import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.CreatePlatformUserDTO;
import com.unosquare.carmigo.dto.GrabAuthenticationDTO;
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
import com.unosquare.carmigo.util.AuthenticationUtils;
import com.unosquare.carmigo.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlatformUserService
{
    private static final int INITIAL_USER_STATUS = 1;

    private final PlatformUserRepository platformUserRepository;
    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final UserSecurityService userSecurityService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    public GrabAuthenticationDTO createAuthenticationToken(final CreateAuthenticationDTO createAuthenticationDTO)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                createAuthenticationDTO.getEmail(), createAuthenticationDTO.getPassword()));
        final UserDetails userDetails = userSecurityService.loadUserByUsername(createAuthenticationDTO.getEmail());
        final String jwt = jwtTokenUtils.generateToken(userDetails);
        final GrabAuthenticationDTO grabAuthenticationDTO = new GrabAuthenticationDTO();
        grabAuthenticationDTO.setJwt(jwt);
        return grabAuthenticationDTO;
    }

    public GrabPlatformUserDTO getPlatformUserById(final int id, final Authentication authentication)
    {
        final PlatformUser platformUser = findPlatformUserById(id, authentication);
        return modelMapper.map(platformUser, GrabPlatformUserDTO.class);
    }

    public GrabPlatformUserDTO createPlatformUser(final CreatePlatformUserDTO createPlatformUserDTO)
    {
        final PlatformUser platformUser = modelMapper.map(createPlatformUserDTO, PlatformUser.class);
        platformUser.setCreatedDate(Instant.now());
        platformUser.setPassword(bCryptPasswordEncoder.encode(createPlatformUserDTO.getPassword()));
        platformUser.setUserAccessStatus(entityManager.getReference(UserAccessStatus.class, INITIAL_USER_STATUS));
        return modelMapper.map(platformUserRepository.save(platformUser), GrabPlatformUserDTO.class);
    }

    public GrabPlatformUserDTO patchPlatformUser(
            final int id, final JsonPatch patch, final Authentication authentication)
    {
        final GrabPlatformUserDTO grabPlatformUserDTO = modelMapper.map(
                findPlatformUserById(id, authentication), GrabPlatformUserDTO.class);
        try {
            final JsonNode platformUserNode = patch.apply(
                    objectMapper.convertValue(grabPlatformUserDTO, JsonNode.class));
            final PlatformUser patchedPlatformUser = objectMapper.treeToValue(platformUserNode, PlatformUser.class);
            return modelMapper.map(platformUserRepository.save(patchedPlatformUser), GrabPlatformUserDTO.class);
        } catch (final JsonPatchException | JsonProcessingException ex) {
            throw new ResourceNotFoundException("Error updating user id " + id);
        }
    }

    public void deletePlatformUserById(final int id, final Authentication authentication)
    {
        findPlatformUserById(id, authentication);
        platformUserRepository.deleteById(id);
    }

    public GrabDriverDTO getDriverById(final int id, final Authentication authentication)
    {
        final Driver driver = findDriverById(id, authentication);
        return modelMapper.map(driver, GrabDriverDTO.class);
    }

    public GrabDriverDTO createDriver(
            final int id, final CreateDriverDTO createDriverDTO, final Authentication authentication)
    {
        final Driver driver = modelMapper.map(createDriverDTO, Driver.class);
        driver.setPlatformUser(entityManager.getReference(PlatformUser.class, id));
        AuthenticationUtils.verifyUserPermission(driver.getPlatformUser().getEmail(), authentication);
        return modelMapper.map(driverRepository.save(driver), GrabDriverDTO.class);
    }

    public void deleteDriverById(final int id, final Authentication authentication)
    {
        findDriverById(id, authentication);
        driverRepository.deleteById(id);
    }

    public GrabPassengerDTO getPassengerById(final int id, final Authentication authentication)
    {
        final Passenger passenger = findPassengerById(id, authentication);
        return modelMapper.map(passenger, GrabPassengerDTO.class);
    }

    public GrabPassengerDTO createPassenger(final int id, final Authentication authentication)
    {
        final Passenger passenger = new Passenger();
        passenger.setPlatformUser(entityManager.getReference(PlatformUser.class, id));
        AuthenticationUtils.verifyUserPermission(passenger.getPlatformUser().getEmail(), authentication);
        return modelMapper.map(passengerRepository.save(passenger), GrabPassengerDTO.class);
    }

    public void deletePassengerById(final int id, final Authentication authentication)
    {
        findPassengerById(id, authentication);
        passengerRepository.deleteById(id);
    }

    private PlatformUser findPlatformUserById(final int id, final Authentication authentication)
    {
        final Optional<PlatformUser> optionalPlatformUser = platformUserRepository.findById(id);
        if (optionalPlatformUser.isPresent()) {
            AuthenticationUtils.verifyUserPermission(optionalPlatformUser.get().getEmail(), authentication);
            return optionalPlatformUser.get();
        }
        throw new EntityNotFoundException(String.format("PlatformUser id %d not found.", id));
    }

    private Driver findDriverById(final int id, final Authentication authentication)
    {
        final Optional<Driver> optionalDriver = driverRepository.findById(id);
        if (optionalDriver.isPresent()) {
            AuthenticationUtils.verifyUserPermission(optionalDriver.get().getPlatformUser().getEmail(), authentication);
            return optionalDriver.get();
        }
        throw new EntityNotFoundException(String.format("Driver id %d not found.", id));
    }

    private Passenger findPassengerById(final int id, final Authentication authentication)
    {
        final Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            AuthenticationUtils.verifyUserPermission(optionalPassenger.get().getPlatformUser().getEmail(), authentication);
            return optionalPassenger.get();
        }
        throw new EntityNotFoundException(String.format("Passenger id %d not found.", id));
    }
}
