package com.unosquare.carmigo.dto;

import com.unosquare.carmigo.model.response.PassengerViewModel;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class GrabJourneyPassengerDTO
{
    private int id;

    private Instant createdDate;

    private GrabLocationDTO locationFrom;

    private GrabLocationDTO locationTo;

    private int maxPassengers;

    private Instant dateTime;

    private GrabDriverDTO driver;

    private List<PassengerViewModel> passengers;
}
