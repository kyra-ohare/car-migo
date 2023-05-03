package com.unosquare.carmigo.service;

import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.model.response.PassengerResponse;
import com.unosquare.carmigo.repository.PassengerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerService {

  private static final String PASSENGER_NOT_FOUND = "Passenger not found";

  private final PassengerRepository passengerRepository;
  private final ModelMapper modelMapper;
  private final EntityManager entityManager;

  public PassengerResponse getPassengerById(final int passengerId) {
    return modelMapper.map(findPassengerById(passengerId), PassengerResponse.class);
  }

  public PassengerResponse createPassengerById(final int passengerId) {
    try {
      findPassengerById(passengerId);
    } catch (final EntityNotFoundException ex) {
      final Passenger passenger = new Passenger();
      passenger.setId(passengerId);
      passenger.setPlatformUser(entityManager.getReference(PlatformUser.class, passengerId));
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

  public void deletePassengerById(final int passengerId) {
    passengerRepository.deleteById(passengerId);
  }

  public Passenger findPassengerById(final int passengerId) {
    return passengerRepository.findById(passengerId).orElseThrow(
        () -> new EntityNotFoundException(PASSENGER_NOT_FOUND));
  }
}
