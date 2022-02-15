package com.unosquare.carmigo.dto;

import lombok.Data;

@Data
public class CreatePassengerJourneyDTO
{
    private int id;

    private int passengerId;

    private int journeyId;
}
