package com.unosquare.carmigo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.unosquare.carmigo.dto.CreatePlatformUserDTO;
import com.unosquare.carmigo.dto.GrabPlatformUserDTO;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.entity.UserAccessStatus;
import com.unosquare.carmigo.exception.PatchException;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import java.time.Instant;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlatformUserService {

  private static final int INITIAL_USER_STATUS = 1;
  private static final String USER_NOT_FOUND = "User not found";

  private final PlatformUserRepository platformUserRepository;
  private final ModelMapper modelMapper;
  private final ObjectMapper objectMapper;
  private final EntityManager entityManager;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public GrabPlatformUserDTO getPlatformUserById(final int userId) {
    return modelMapper.map(findPlatformUserById(userId), GrabPlatformUserDTO.class);
  }

  public GrabPlatformUserDTO createPlatformUser(final CreatePlatformUserDTO createPlatformUserDTO) {
    final PlatformUser platformUser = modelMapper.map(createPlatformUserDTO, PlatformUser.class);
    platformUser.setCreatedDate(Instant.now());
    platformUser.setPassword(bCryptPasswordEncoder.encode(createPlatformUserDTO.getPassword()));
    platformUser.setUserAccessStatus(entityManager.getReference(UserAccessStatus.class, INITIAL_USER_STATUS));
    final PlatformUser newPlatformUser;
    try {
      newPlatformUser = platformUserRepository.save(platformUser);
    } catch (final DataIntegrityViolationException ex) {
      throw new EntityExistsException("Email already in use");
    }
    return modelMapper.map(newPlatformUser, GrabPlatformUserDTO.class);
  }

  public GrabPlatformUserDTO patchPlatformUserById(final int userId, final JsonPatch patch) {
    final GrabPlatformUserDTO grabPlatformUserDTO = modelMapper.map(
        findPlatformUserById(userId), GrabPlatformUserDTO.class);
    try {
      final JsonNode platformUserNode = patch.apply(objectMapper.convertValue(grabPlatformUserDTO, JsonNode.class));
      final PlatformUser patchedPlatformUser = objectMapper.treeToValue(platformUserNode, PlatformUser.class);
      return modelMapper.map(platformUserRepository.save(patchedPlatformUser), GrabPlatformUserDTO.class);
    } catch (final JsonPatchException | JsonProcessingException ex) {
      throw new PatchException(String.format("Error updating user - %s", ex.getMessage()));
    }
  }

  public void deletePlatformUserById(final int userId) {
    try {
      platformUserRepository.deleteById(userId);
    } catch (final EmptyResultDataAccessException ex) {
      throw new EntityNotFoundException(USER_NOT_FOUND);
    }
  }

  private PlatformUser findPlatformUserById(final int userId) {
    return platformUserRepository.findById(userId).orElseThrow(
        () -> new EntityNotFoundException(USER_NOT_FOUND));
  }
}
