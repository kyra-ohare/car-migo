package com.unosquare.carmigo.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
public class CreateJourneyViewModel
{
    @NotBlank
    @JsonProperty("locationFrom")
    private int locationIdFrom;

    @NotBlank
    @JsonProperty("locationTo")
    private int locationIdTo;

    @NotBlank
    @JsonProperty("maxPassengers")
    private int maxPassengers;

    @NotBlank
    @JsonProperty("dateTime")
    private Instant dateTime;

    @NotBlank
    @JsonProperty("driverId")
    private int driver;
}
