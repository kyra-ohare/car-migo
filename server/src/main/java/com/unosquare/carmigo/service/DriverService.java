package com.unosquare.carmigo.service;

import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.dto.request.DriverRequest;
import com.unosquare.carmigo.dto.response.DriverResponse;
import com.unosquare.carmigo.repository.DriverRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {

  private static final String DRIVER_NOT_FOUND = "Driver not found";

  private final DriverRepository driverRepository;
  private final ModelMapper modelMapper;
  private final EntityManager entityManager;

  public DriverResponse getDriverById(final int driverId) {
    return modelMapper.map(findDriverById(driverId), DriverResponse.class);
  }

  public DriverResponse createDriverById(final int driverId, final DriverRequest driverRequest) {
    try {
      findDriverById(driverId);
    } catch (final EntityNotFoundException ex) {
      final Driver driver = modelMapper.map(driverRequest, Driver.class);
      driver.setId(driverId);
      driver.setPlatformUser(entityManager.getReference(PlatformUser.class, driverId));
      final Driver newDriver;
      try {
        newDriver = driverRepository.save(driver);
      } catch (final DataIntegrityViolationException e) {
        throw new EntityNotFoundException("Non-existent user to create a driver");
      }
      return modelMapper.map(newDriver, DriverResponse.class);
    }
    throw new EntityExistsException("Driver already exists");
  }

  public void deleteDriverById(final int driverId) {
    driverRepository.deleteById(driverId);
  }

  private Driver findDriverById(final int driverId) {
    return driverRepository.findById(driverId).orElseThrow(
      () -> new EntityNotFoundException(DRIVER_NOT_FOUND));
  }
}
