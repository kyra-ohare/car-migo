package com.unosquare.carmigo.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class GrabJourneyDriverDTO
{
    private int id;

    private Instant createdDate;

    private GrabLocationDTO locationFrom;

    private GrabLocationDTO locationTo;

    private int maxPassengers;

    private Instant dateTime;

    private GrabDriverDTO driver;
}
