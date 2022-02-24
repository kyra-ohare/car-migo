package com.unosquare.carmigo.service;

import com.unosquare.carmigo.dto.CreatePassengerDTO;
import com.unosquare.carmigo.dto.GrabPassengerDTO;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class PassengerService
{
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    public GrabPassengerDTO createPassenger(final int id, final CreatePassengerDTO createPassengerDTO)
    {
        final Passenger passenger = modelMapper.map(createPassengerDTO, Passenger.class);
        passenger.setPlatformUser(entityManager.getReference(PlatformUser.class, id));
        return modelMapper.map(passengerRepository.save(passenger), GrabPassengerDTO.class);
    }

    public void deletePassengerById(final int id)
    {
        passengerRepository.deleteById(id);
    }
}
