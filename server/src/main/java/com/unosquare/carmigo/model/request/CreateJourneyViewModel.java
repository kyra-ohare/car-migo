package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class CreateJourneyViewModel
{
    @NotNull
    @JsonProperty("locationIdFrom")
    private int locationIdFrom;

    @NotNull
    @JsonProperty("locationIdTo")
    private int locationIdTo;

    @NotNull
    @JsonProperty("maxPassengers")
    private int maxPassengers;

    @NotNull
    @JsonProperty("dateTime")
    private Instant dateTime;

    @NotNull
    @JsonProperty("driverId")
    private int driverId;
}
