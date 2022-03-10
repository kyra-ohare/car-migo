package com.unosquare.carmigo.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class GrabJourneyDTO
{
    private int id;

    private Instant createdDate;

    private GrabLocationDTO locationIdFrom;

    private GrabLocationDTO locationIdTo;

    private int maxPassengers;

    private Instant dateTime;

    private GrabDriverDTO driver;
}
