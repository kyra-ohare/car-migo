package com.unosquare.carmigo.service;

import static com.unosquare.carmigo.util.CommonBehaviours.findEntityById;

import com.unosquare.carmigo.dto.request.DriverRequest;
import com.unosquare.carmigo.dto.response.DriverResponse;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.DriverRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Handles requests regarding the {@link Driver} entity.
 */
@Service
@RequiredArgsConstructor
public class DriverService {

  private static final String DRIVER_CACHE = "driver";
  static final String DRIVER_NOT_FOUND = "Driver not found";

  private final DriverRepository driverRepository;
  private final ModelMapper modelMapper;
  private final EntityManager entityManager;

  /**
   * Creates a driver.
   *
   * @param platformUserId the platform user id to create a driver.
   * @param driverRequest  the requirements as {@link DriverRequest}.
   * @return a {@link DriverResponse}.
   */
  @CachePut(value = DRIVER_CACHE, key = "#result.id")
  public DriverResponse createDriverById(final int platformUserId, final DriverRequest driverRequest) {
    try {
      findEntityById(platformUserId, driverRepository, DRIVER_NOT_FOUND);
    } catch (final EntityNotFoundException ex) {
      final Driver driver = modelMapper.map(driverRequest, Driver.class);
      driver.setId(platformUserId);
      driver.setPlatformUser(entityManager.getReference(PlatformUser.class, platformUserId));
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

  /**
   * Fetches a driver.
   *
   * @param driverId the driver id to search for.
   * @return a {@link DriverResponse}.
   */
  @Cacheable(value = DRIVER_CACHE, key = "#driverId")
  public DriverResponse getDriverById(final int driverId) {
    return modelMapper.map(findEntityById(driverId, driverRepository, DRIVER_NOT_FOUND), DriverResponse.class);
  }

  /**
   * Deletes a driver. The platform user is not affected.
   *
   * @param driverId the driver id to be deleted.
   */
  @CacheEvict(value = DRIVER_CACHE, key = "#driverId")
  public void deleteDriverById(final int driverId) {
    driverRepository.deleteById(driverId);
  }
}
