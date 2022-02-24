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
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PlatformUserService
{
    private static final int INITIAL_USER_STATUS = 1;

    private final PlatformUserRepository platformUserRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    public GrabPlatformUserDTO getPlatformUserById(final int id)
    {   // getById / findById
        final PlatformUser platformUser = platformUserRepository.findById(id)   // LazyInitializationException
                .orElseThrow(() -> new ResourceNotFoundException("User id not found."));
//        return modelMapper.map(platformUser, GrabPlatformUserDTO.class);
        System.out.println(platformUser.getUserAccessStatus().getStatus());
        return new GrabPlatformUserDTO();
//        return modelMapper.map(platformUser, GrabPlatformUserDTO.class);
    }

    public GrabPlatformUserDTO createPlatformUser(final CreatePlatformUserDTO createPlatformUserDTO)
    {
        PlatformUser platformUser = modelMapper.map(createPlatformUserDTO, PlatformUser.class);

        platformUser.setCreatedDate(Instant.now());
//        final TypedQuery<UserAccessStatus> query = entityManager.createQuery(
//                "SELECT uss FROM UserAccessStatus uss WHERE uss.id = " + INITIAL_USER_STATUS,
//                UserAccessStatus.class);
//        final UserAccessStatus userAccessStatus = query.getSingleResult();

        final UserAccessStatus userAccessStatus = entityManager.find(UserAccessStatus.class, INITIAL_USER_STATUS);
        platformUser.setUserAccessStatus(userAccessStatus);

        platformUser = platformUserRepository.save(platformUser);
        return modelMapper.map(platformUser, GrabPlatformUserDTO.class);
//        try {
//        } catch(final DataIntegrityViolationException ex) {
//            throw new ResourceNotFoundException("Email already exists");
//        }
    }

    public GrabPlatformUserDTO updatePlatformUser(final int id, JsonPatch patch)
    {
        final PlatformUser targetPlatformUser = modelMapper.map(getPlatformUserById(id), PlatformUser.class);
        try {
            final PlatformUser patchedPlatformUser = applyPatchToPlatformUser(patch, targetPlatformUser);
            return modelMapper.map(platformUserRepository.save(patchedPlatformUser), GrabPlatformUserDTO.class);
        } catch (JsonPatchException | JsonProcessingException ex) {
            throw new ResourceNotFoundException("Error updating User");
        }
    }

    public void deletePlatformUserById(final int id)
    {
        platformUserRepository.deleteById(id);
    }

    private PlatformUser applyPatchToPlatformUser(final JsonPatch patch, final PlatformUser targetPlatformUser)
            throws JsonPatchException, JsonProcessingException
    {
        final JsonNode patched = patch.apply(objectMapper.convertValue(targetPlatformUser, JsonNode.class));
        return objectMapper.treeToValue(patched, PlatformUser.class);
    }
}
