package com.unosquare.carmigo.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CreateJourneyDTO
{
    private int id;

    private int locationIdFrom;

    private int locationIdTo;

    private int maxPassengers;

    private Timestamp dateTime;

    private int driverId;
}
