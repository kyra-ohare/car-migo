package com.unosquare.carmigo.service;

import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.PassengerRepository;
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
public class PassengerService {

  private static final String PASSENGER_NOT_FOUND = "Passenger not found";

  private final PassengerRepository passengerRepository;
  private final ModelMapper modelMapper;
  private final EntityManager entityManager;

  public GrabPassengerDTO getPassengerById(final int passengerId) {
    return modelMapper.map(findPassengerById(passengerId), GrabPassengerDTO.class);
  }

  public GrabPassengerDTO createPassengerById(final int passengerId) {
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
      return modelMapper.map(newPassenger, GrabPassengerDTO.class);
    }
    throw new EntityExistsException("Passenger already exists");
  }

  public void deletePassengerById(final int passengerId) {
    try {
      passengerRepository.deleteById(passengerId);
    } catch (final EmptyResultDataAccessException ex) {
      throw new EntityNotFoundException(PASSENGER_NOT_FOUND);
    }
  }

  public Passenger findPassengerById(final int passengerId) {
    return passengerRepository.findById(passengerId).orElseThrow(
        () -> new EntityNotFoundException(PASSENGER_NOT_FOUND));
  }
}
