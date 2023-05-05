package com.unosquare.carmigo.service;

import static com.unosquare.carmigo.security.UserStatus.ACTIVE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.unosquare.carmigo.dto.PlatformUserDto;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.entity.UserAccessStatus;
import com.unosquare.carmigo.exception.PatchException;
import com.unosquare.carmigo.model.request.PlatformUserRequest;
import com.unosquare.carmigo.model.response.PlatformUserResponse;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlatformUserService {

  private static final int STAGED_USER_STATUS = 1;
  private static final int ACTIVE_USER_STATUS = 2;
  private static final String USER_NOT_FOUND = "User not found";

  private final PlatformUserRepository platformUserRepository;
  private final ModelMapper modelMapper;
  private final ObjectMapper objectMapper;
  private final EntityManager entityManager;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

  public PlatformUserResponse getPlatformUserById(final int userId) {
    return modelMapper.map(findPlatformUserById(userId), PlatformUserResponse.class);
  }

  public PlatformUserResponse createPlatformUser(final PlatformUserRequest platformUserRequest) {
    final PlatformUser platformUser = modelMapper.map(platformUserRequest, PlatformUser.class);
    platformUser.setCreatedDate(Instant.now());
    platformUser.setPassword(bCryptPasswordEncoder.encode(platformUserRequest.getPassword()));
    platformUser.setUserAccessStatus(entityManager.getReference(UserAccessStatus.class, STAGED_USER_STATUS));
    final PlatformUser newPlatformUser;
    try {
      newPlatformUser = platformUserRepository.save(platformUser);
    } catch (final DataIntegrityViolationException ex) {
      throw new EntityExistsException("Email already in use");
    }
    return modelMapper.map(newPlatformUser, PlatformUserResponse.class);
  }

  public PlatformUserResponse patchPlatformUserById(final int userId, final JsonPatch patch) {
    final PlatformUserDto platformUserDto = modelMapper.map(
    findPlatformUserById(userId), PlatformUserDto.class);
    final PlatformUser savedPlatformUser;
    try {
      final JsonNode platformUserNode = patch.apply(objectMapper.convertValue(platformUserDto, JsonNode.class));
      final PlatformUser patchedPlatformUser = objectMapper.treeToValue(platformUserNode, PlatformUser.class);
      savedPlatformUser = platformUserRepository.save(patchedPlatformUser);
    } catch (final JsonPatchException | JsonProcessingException | DataIntegrityViolationException ex) {
      throw new PatchException(String.format("Error updating user - %s", ex.getMessage()));
    }
    return modelMapper.map(savedPlatformUser, PlatformUserResponse.class);
  }

  public void deletePlatformUserById(final int userId) {
    platformUserRepository.deleteById(userId);
  }

  private PlatformUser findPlatformUserById(final int userId) {
    return platformUserRepository.findById(userId).orElseThrow(
        () -> new EntityNotFoundException(USER_NOT_FOUND));
  }
}
