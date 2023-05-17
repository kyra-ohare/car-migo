package com.unosquare.carmigo.service;

import com.unosquare.carmigo.dto.response.PassengerResponse;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.PassengerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Handles requests regarding the {@link Passenger} entity.
 */
@Service
@RequiredArgsConstructor
public class PassengerService {

  private static final String PASSENGER_NOT_FOUND = "Passenger not found";

  private final PassengerRepository passengerRepository;
  private final ModelMapper modelMapper;
  private final EntityManager entityManager;

  /**
   * Creates a passenger.
   *
   * @param platformUserId the platform user id to create a passenger.
   * @return a {@link PassengerResponse}.
   */
  public PassengerResponse createPassengerById(final int platformUserId) {
    try {
      findPassengerById(platformUserId);
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
  public PassengerResponse getPassengerById(final int passengerId) {
    return modelMapper.map(findPassengerById(passengerId), PassengerResponse.class);
  }

  /**
   * Deletes a passenger. The platform user is not affected.
   *
   * @param passengerId the passenger id to be deleted.
   */
  public void deletePassengerById(final int passengerId) {
    passengerRepository.deleteById(passengerId);
  }

  /**
   * Fetches a passenger from the database by their id.
   *
   * @param passengerId the passenger id to search for.
   * @return a {@link Passenger} entity.
   */
  public Passenger findPassengerById(final int passengerId) {
    return passengerRepository.findById(passengerId).orElseThrow(
        () -> new EntityNotFoundException(PASSENGER_NOT_FOUND));
  }
}
