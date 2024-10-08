package com.unosquare.carmigo.service;

import static com.unosquare.carmigo.constant.AppConstants.PLATFORM_USER_CACHE;
import static com.unosquare.carmigo.security.UserStatus.ACTIVE;
import static com.unosquare.carmigo.util.CommonBehaviours.findEntityById;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.unosquare.carmigo.dto.PlatformUserDto;
import com.unosquare.carmigo.dto.request.PlatformUserRequest;
import com.unosquare.carmigo.dto.response.PlatformUserResponse;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.entity.UserAccessStatus;
import com.unosquare.carmigo.exception.PatchException;
import com.unosquare.carmigo.repository.DriverRepository;
import com.unosquare.carmigo.repository.PassengerRepository;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles requests regarding the {@link PlatformUser} entity.
 */
@Service
@RequiredArgsConstructor
public class PlatformUserService {

  private static final int STAGED_USER_STATUS = 1;
  private static final int ACTIVE_USER_STATUS = 2;
  private static final String USER_NOT_FOUND = "User not found!";

  private final PlatformUserRepository platformUserRepository;
  private final DriverRepository driverRepository;
  private final PassengerRepository passengerRepository;
  private final ModelMapper modelMapper;
  private final ObjectMapper objectMapper;
  private final EntityManager entityManager;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  /**
   * Creates a platform user. This new user's access status is set to STAGED.
   *
   * @param platformUserRequest the requirements as {@link PlatformUserRequest}.
   * @return a {@link PlatformUserResponse}.
   */
  @CachePut(value = PLATFORM_USER_CACHE, key = "#result.id")
  public PlatformUserResponse createPlatformUser(final PlatformUserRequest platformUserRequest) {
    final PlatformUser platformUser = modelMapper.map(platformUserRequest, PlatformUser.class);
    platformUser.setCreatedDate(Instant.now());
    platformUser.setPassword(bCryptPasswordEncoder.encode(platformUserRequest.getPassword()));
    platformUser.setUserAccessStatus(entityManager.getReference(UserAccessStatus.class, STAGED_USER_STATUS));
    final PlatformUser newPlatformUser;
    try {
      newPlatformUser = platformUserRepository.save(platformUser);
    } catch (final DataIntegrityViolationException ex) {
      throw new EntityExistsException("Email already in use.");
    }
    return modelMapper.map(newPlatformUser, PlatformUserResponse.class);
  }

  /**
   * Allows a user to confirm their email.
   * Upon confirmation, their access status is set to ACTIVE which gives them more access.
   *
   * @param email the user's email.
   */
  public void confirmEmail(final String email) {
    final Optional<PlatformUser> platformUserOptional = platformUserRepository.findPlatformUserByEmail(email);
    if (platformUserOptional.isEmpty()) {
      throw new EntityNotFoundException(USER_NOT_FOUND);
    }
    if (platformUserOptional.get().getUserAccessStatus().getStatus().equals(ACTIVE.name())) {
      throw new IllegalStateException("User is already active");
    }
    platformUserOptional.get()
        .setUserAccessStatus(entityManager.getReference(UserAccessStatus.class, ACTIVE_USER_STATUS));
    platformUserRepository.save(platformUserOptional.get());
  }

  /**
   * Fetches a platform user and caches them.
   *
   * @param platformUserId the platform user id to search for.
   * @return a {@link PlatformUserResponse}.
   */
  @Cacheable(value = PLATFORM_USER_CACHE, key = "#platformUserId")
  public PlatformUserResponse getPlatformUserById(final int platformUserId) {
    final var platformUser = findEntityById(platformUserId, platformUserRepository, USER_NOT_FOUND);
    final var response = modelMapper.map(platformUser, PlatformUserResponse.class);
    try {
      findEntityById(platformUserId, driverRepository, "");
      response.setDriver(true);
    } catch (EntityNotFoundException ex) {
      response.setDriver(false);
    }
    try {
      findEntityById(platformUserId, passengerRepository, "");
      response.setPassenger(true);
    } catch (EntityNotFoundException ex) {
      response.setPassenger(false);
    }
    return response;
  }

  /**
   * Corrects platform user information.<br>
   * Pass an array of a {@link JsonPatch} body with the operation, the path and the value.<br>
   * Accepted operation values are “add”, "remove", "replace", "move", "copy" and "test".<br>
   * Here is an example which updates a user's phone number and their access status:<br>
   * <pre>
   *   [
   *     {
   *       "op": "replace",
   *       "path": "/phoneNumber",
   *       "value": "02875935862"
   *     },
   *     {
   *       "op": "replace",
   *       "path": "/userAccessStatus/id",
   *       "value": "5"
   *     }
   *   ]
   * </pre>
   *
   * @param platformUserId the platform user id to be updated.
   * @param patch          a {@link JsonPatch}.
   * @return a {@link PlatformUserResponse}.
   */
  @CachePut(value = PLATFORM_USER_CACHE, key = "#platformUserId")
  public PlatformUserResponse patchPlatformUserById(final int platformUserId, final JsonPatch patch) {
    final PlatformUser platformUser = findEntityById(platformUserId, platformUserRepository, USER_NOT_FOUND);
    final PlatformUserDto platformUserDto = modelMapper.map(platformUser, PlatformUserDto.class);
    final PlatformUser savedPlatformUser;
    try {
      final JsonNode platformUserNode = patch.apply(objectMapper.convertValue(platformUserDto, JsonNode.class));
      final PlatformUser patchedPlatformUser = objectMapper.treeToValue(platformUserNode, PlatformUser.class);
      savedPlatformUser = platformUserRepository.save(patchedPlatformUser);
    } catch (final JsonPatchException | JsonProcessingException | DataIntegrityViolationException ex) {
      throw new PatchException("Error updating user - %s".formatted(ex.getMessage()));
    }
    return modelMapper.map(savedPlatformUser, PlatformUserResponse.class);
  }

  /**
   * Deletes a platform user.<br>
   * <strong>Warning:</strong> it will also delete (if any) the driver and/or the passenger associated.
   * Example: Tom is a platform user who is also a driver as well as a passenger. When deleting Tom's platform user
   * profile, his driver's and passenger's profiles will also be deleted.
   *
   * @param platformUserId the platform user id to be deleted.
   */
  @CacheEvict(value = PLATFORM_USER_CACHE, key = "#platformUserId")
  public void deletePlatformUserById(final int platformUserId) {
    platformUserRepository.deleteById(platformUserId);
  }
}
