package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class JourneyViewModel
{
    @JsonProperty("id")
    private int id;

    @JsonProperty("createdDate")
    private Instant createdDate;

    @JsonProperty("locationFrom")
    private LocationViewModel locationFrom;

    @JsonProperty("locationTo")
    private LocationViewModel locationTo;

    @JsonProperty("maxPassengers")
    private int maxPassengers;

    @JsonProperty("dateTime")
    private Instant dateTime;

    @JsonProperty("driver")
    private DriverViewModel driver;
}
