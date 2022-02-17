package com.unosquare.carmigo.service;

import com.unosquare.carmigo.dto.CreatePlatformUserDTO;
import com.unosquare.carmigo.dto.GetPlatformUserDTO;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlatformUserService
{
    private final PlatformUserRepository platformUserRepository;
    private final ModelMapper modelMapper;

    public GetPlatformUserDTO getPlatformUserById(final int id)
    {
        final PlatformUser platformUser = platformUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id not found."));
        return modelMapper.map(platformUser, GetPlatformUserDTO.class);
    }

    public GetPlatformUserDTO createPlatformUser(final CreatePlatformUserDTO createPlatformUserDTO)
    {
        final PlatformUser platformUser = modelMapper.map(createPlatformUserDTO, PlatformUser.class);
        return modelMapper.map(platformUserRepository.save(platformUser), GetPlatformUserDTO.class);
    }

    public void updatePlatformUser(final int id)
    {
        final Optional<PlatformUser> platformUser = platformUserRepository.findById(id);

    }

    public void deletePlatformUserById(final int id)
    {
        platformUserRepository.deleteById(id);
    }
}
