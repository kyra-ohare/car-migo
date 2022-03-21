package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Data;

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
    private int driver;
}
