package com.unosquare.carmigo.service;

import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class DriverService
{
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

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
}
