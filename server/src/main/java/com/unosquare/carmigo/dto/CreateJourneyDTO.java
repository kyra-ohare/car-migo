package com.unosquare.carmigo.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CreateJourneyDTO
{
    private int locationIdFrom;

    private int locationIdTo;

    private int maxPassengers;

    private Instant dateTime;

    private int driverId;
}
