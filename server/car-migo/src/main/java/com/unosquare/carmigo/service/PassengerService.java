package com.unosquare.carmigo.service;

import static com.unosquare.carmigo.constant.AppConstants.PASSENGER_CACHE;
import static com.unosquare.carmigo.constant.AppConstants.PLATFORM_USER_CACHE;
import static com.unosquare.carmigo.util.CommonBehaviours.findEntityById;

import com.unosquare.carmigo.dto.response.PassengerResponse;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.PassengerRepository;
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
 * Handles requests regarding the {@link Passenger} entity.
 */
@Service
@RequiredArgsConstructor
public class PassengerService {

  protected static final String PASSENGER_NOT_FOUND = "Passenger not found";

  private final PassengerRepository passengerRepository;
  private final ModelMapper modelMapper;
  private final EntityManager entityManager;

  /**
   * Creates a passenger.
   *
   * @param platformUserId the platform user id to create a passenger.
   * @return a {@link PassengerResponse}.
   */
  @CachePut(value = PASSENGER_CACHE, key = "#result.id")
  @CacheEvict(value = PLATFORM_USER_CACHE, key = "#result.id")
  public PassengerResponse createPassengerById(final int platformUserId) {
    try {
      findEntityById(platformUserId, passengerRepository, PASSENGER_NOT_FOUND);
    } catch (final EntityNotFoundException ex) {
      final Passenger passenger = new Passenger();
      passenger.setId(platformUserId);
      passenger.setPlatformUser(entityManager.getReference(PlatformUser.class, platformUserId));
      final Passenger newPassenger;
      try {
        newPassenger = passengerRepository.save(passenger);
      } catch (final DataIntegrityViolationException e) {
        throw new EntityNotFoundException("Non-existent user to create a passenger");
      }
      return modelMapper.map(newPassenger, PassengerResponse.class);
    }
    throw new EntityExistsException("Passenger already exists");
  }

  /**
   * Fetches a passenger.
   *
   * @param passengerId the passenger id to search for.
   * @return a {@link PassengerResponse}.
   */
  @Cacheable(value = PASSENGER_CACHE, key = "#passengerId")
  public PassengerResponse getPassengerById(final int passengerId) {
    final var passenger = findEntityById(passengerId, passengerRepository, PASSENGER_NOT_FOUND);
    return modelMapper.map(passenger, PassengerResponse.class);
  }

  /**
   * Deletes a passenger. The platform user is not affected.
   *
   * @param passengerId the passenger id to be deleted.
   */
  @CacheEvict(value = {PASSENGER_CACHE, PLATFORM_USER_CACHE}, key = "#passengerId")
  public void deletePassengerById(final int passengerId) {
    passengerRepository.deleteById(passengerId);
  }
}
