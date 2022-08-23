package com.unosquare.carmigo.service;

import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.DriverRepository;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {

  private static final String DRIVER_NOT_FOUND = "Driver not found";

  private final DriverRepository driverRepository;
  private final ModelMapper modelMapper;
  private final EntityManager entityManager;

  public GrabDriverDTO getDriverById(final int driverId) {
    return modelMapper.map(findDriverById(driverId), GrabDriverDTO.class);
  }

  public GrabDriverDTO createDriverById(final int driverId, final CreateDriverDTO createDriverDTO) {
    try {
      findDriverById(driverId);
    } catch (final EntityNotFoundException ex) {
      final Driver driver = modelMapper.map(createDriverDTO, Driver.class);
      driver.setId(driverId);
      driver.setPlatformUser(entityManager.getReference(PlatformUser.class, driverId));
      final Driver newDriver;
      try {
        newDriver = driverRepository.save(driver);
      } catch (final DataIntegrityViolationException e) {
        throw new EntityNotFoundException("Non-existent user to create a driver");
      }
      return modelMapper.map(newDriver, GrabDriverDTO.class);
    }
    throw new EntityExistsException("Driver already exists");
  }

  public void deleteDriverById(final int driverId) {
    try {
      driverRepository.deleteById(driverId);
    } catch (final EmptyResultDataAccessException ex) {
      throw new EntityNotFoundException(DRIVER_NOT_FOUND);
    }
  }

  private Driver findDriverById(final int driverId) {
    return driverRepository.findById(driverId).orElseThrow(
        () -> new EntityNotFoundException(DRIVER_NOT_FOUND));
  }
}
