package com.unosquare.carmigo.service;

import static com.unosquare.carmigo.constant.AppConstants.ENTITY_NOT_FOUND_ERROR_MSG;

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
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.security.UserSecurityService;
import com.unosquare.carmigo.util.AuthenticationUtils;
import com.unosquare.carmigo.util.JwtTokenUtils;
import java.time.Instant;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

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
  private final AppUser appUser;

  public GrabAuthenticationDTO createAuthenticationToken(final CreateAuthenticationDTO createAuthenticationDTO) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        createAuthenticationDTO.getEmail(), createAuthenticationDTO.getPassword()));
    final UserDetails userDetails = userSecurityService.loadUserByUsername(createAuthenticationDTO.getEmail());
    final String jwt = jwtTokenUtils.generateToken(userDetails);
    final GrabAuthenticationDTO grabAuthenticationDTO = new GrabAuthenticationDTO();
    grabAuthenticationDTO.setJwt(jwt);
    return grabAuthenticationDTO;
  }

  public GrabPlatformUserDTO getPlatformUserById(final int id) {
    final PlatformUser platformUser = findEntityById(
        getCurrentId(id), platformUserRepository, PlatformUser.class.getSimpleName());
    return modelMapper.map(platformUser, GrabPlatformUserDTO.class);
  }

  public GrabPlatformUserDTO createPlatformUser(final CreatePlatformUserDTO createPlatformUserDTO) {
    final PlatformUser platformUser = modelMapper.map(createPlatformUserDTO, PlatformUser.class);
    platformUser.setCreatedDate(Instant.now());
    platformUser.setPassword(bCryptPasswordEncoder.encode(createPlatformUserDTO.getPassword()));
    platformUser.setUserAccessStatus(entityManager.getReference(UserAccessStatus.class, INITIAL_USER_STATUS));
    return modelMapper.map(platformUserRepository.save(platformUser), GrabPlatformUserDTO.class);
  }

  public GrabPlatformUserDTO patchPlatformUser(final int id, final JsonPatch patch) {
    final int currentId = getCurrentId(id);
    final PlatformUser platformUser = findEntityById(
        currentId, platformUserRepository, PlatformUser.class.getSimpleName());
    final GrabPlatformUserDTO grabPlatformUserDTO = modelMapper.map(platformUser, GrabPlatformUserDTO.class);
    try {
      final JsonNode platformUserNode = patch.apply(objectMapper.convertValue(grabPlatformUserDTO, JsonNode.class));
      final PlatformUser patchedPlatformUser = objectMapper.treeToValue(platformUserNode, PlatformUser.class);
      return modelMapper.map(platformUserRepository.save(patchedPlatformUser), GrabPlatformUserDTO.class);
    } catch (final JsonPatchException | JsonProcessingException ex) {
      throw new ResourceNotFoundException("Error updating user id " + currentId);
    }
  }

  public void deletePlatformUserById(final int id) {
    final int currentId = getCurrentId(id);
    findEntityById(currentId, platformUserRepository, PlatformUser.class.getSimpleName());
    platformUserRepository.deleteById(getCurrentId(currentId));
  }

  public GrabDriverDTO getDriverById(final int id) {
    final Driver driver = findEntityById(getCurrentId(id), driverRepository, Driver.class.getSimpleName());
    return modelMapper.map(driver, GrabDriverDTO.class);
  }

  public GrabDriverDTO createDriver(final int id, final CreateDriverDTO createDriverDTO) {
    final int currentId = getCurrentId(id);
    final Driver driver = modelMapper.map(createDriverDTO, Driver.class);
    driver.setPlatformUser(entityManager.getReference(PlatformUser.class, currentId));
    AuthenticationUtils.verifyUserPermission(currentId, appUser.get());
    return modelMapper.map(driverRepository.save(driver), GrabDriverDTO.class);
  }

  public void deleteDriverById(final int id) {
    final int currentId = getCurrentId(id);
    findEntityById(currentId, driverRepository, Driver.class.getSimpleName());
    driverRepository.deleteById(currentId);
  }

  public GrabPassengerDTO getPassengerById(final int id) {
    final Passenger passenger = findEntityById(getCurrentId(id), passengerRepository, Passenger.class.getSimpleName());
    return modelMapper.map(passenger, GrabPassengerDTO.class);
  }

  public GrabPassengerDTO createPassenger(final int id) {
    final int currentId = getCurrentId(id);
    final Passenger passenger = new Passenger();
    passenger.setPlatformUser(entityManager.getReference(PlatformUser.class, currentId));
    AuthenticationUtils.verifyUserPermission(currentId, appUser.get());
    return modelMapper.map(passengerRepository.save(passenger), GrabPassengerDTO.class);
  }

  public void deletePassengerById(final int id) {
    final int currentId = getCurrentId(id);
    findEntityById(currentId, passengerRepository, Passenger.class.getSimpleName());
    passengerRepository.deleteById(currentId);
  }

  private <E> E findEntityById(final int id, final JpaRepository<E, Integer> repository, final String entityName) {
    final Optional<E> optional = repository.findById(id);
    if (optional.isPresent()) {
      AuthenticationUtils.verifyUserPermission(id, appUser.get());
      return optional.get();
    }
    throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_ERROR_MSG, entityName, id));
  }

  private int getCurrentId(final int id) {
    return id != 0 ? id : appUser.get().getId();
  }
}
