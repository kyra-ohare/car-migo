package com.unosquare.carmigo.service;

import com.unosquare.carmigo.dto.CreatePassengerDTO;
import com.unosquare.carmigo.entity.Passenger;
import com.unosquare.carmigo.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerService
{
    private PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;

    public Passenger createPassenger(final CreatePassengerDTO createPassengerDTO)
    {
        final Passenger passenger = modelMapper.map(createPassengerDTO, Passenger.class);
        return passengerRepository.save(passenger);
    }

    public void deletePassengerById(final int id)
    {
        passengerRepository.deleteById(id);
    }
}
