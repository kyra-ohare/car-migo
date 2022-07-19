package com.unosquare.carmigo.service;

import com.unosquare.carmigo.dto.CreateDriverDTO;
import com.unosquare.carmigo.dto.GrabDriverDTO;
import com.unosquare.carmigo.entity.Driver;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.DriverRepository;
import com.unosquare.carmigo.security.Authorization;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {

  private final DriverRepository driverRepository;
  private final ModelMapper modelMapper;
  private final EntityManager entityManager;
  private final Authorization authorization;

  public GrabDriverDTO getDriverById(final int id) {
    return modelMapper.map(findDriverById(id), GrabDriverDTO.class);
  }

  public GrabDriverDTO createDriverById(final int id, final CreateDriverDTO createDriverDTO) {
    try {
      findDriverById(id);
    } catch (final EntityNotFoundException ex) {
      final Driver driver = modelMapper.map(createDriverDTO, Driver.class);
      driver.setId(id);
      driver.setPlatformUser(entityManager.getReference(PlatformUser.class, id));
      return modelMapper.map(driverRepository.save(driver), GrabDriverDTO.class);
    }
    throw new DataIntegrityViolationException(String.format("Driver id %d already exists", id));
  }

  public void deleteDriverById(final int id) {
    findDriverById(id);
    driverRepository.deleteById(id);
  }

  private Driver findDriverById(final int id) {
    authorization.verifyUserAuthorization(id);
    return driverRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Driver id %d not found.", id)));
  }
}
