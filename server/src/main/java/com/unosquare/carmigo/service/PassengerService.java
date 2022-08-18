package com.unosquare.carmigo.service;

import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.PassengerRepository;
import com.unosquare.carmigo.security.AppUser;
import com.unosquare.carmigo.security.Authorization;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerService {

  private final PassengerRepository passengerRepository;
  private final ModelMapper modelMapper;
  private final EntityManager entityManager;
  private final Authorization authorization;

  public GrabPassengerDTO getPassengerById(final int id) {
    return modelMapper.map(findPassengerById(id), GrabPassengerDTO.class);
  }

  public GrabPassengerDTO createPassengerById(final int id) {
    try {
      findPassengerById(id);
    } catch (final EntityNotFoundException ex) {
      final Passenger passenger = new Passenger();
      passenger.setId(id);
      passenger.setPlatformUser(entityManager.getReference(PlatformUser.class, id));
      return modelMapper.map(passengerRepository.save(passenger), GrabPassengerDTO.class);
    }
    throw new DataIntegrityViolationException(String.format("Passenger id %d already exists", id));
  }

  public void deletePassengerById(final int id) {
    findPassengerById(id);
    passengerRepository.deleteById(id);
  }

  public Passenger findPassengerById(final int id) {
    authorization.verifyUserAuthorization(id);
    return passengerRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Passenger id %d not found.", id)));
  }
}
