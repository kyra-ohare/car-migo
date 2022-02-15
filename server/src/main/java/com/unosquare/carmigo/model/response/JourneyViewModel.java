package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JourneyViewModel
{
    @JsonProperty("id")
    private int id;

    @JsonProperty("locationFrom")
    private LocationViewModel locationFrom;

    @JsonProperty("locationTo")
    private LocationViewModel locationTo;

    @JsonProperty("driver")
    private DriverViewModel driver;

    @JsonProperty("maxPassengers")
    private int maxPassengers;

    @JsonProperty("passenger")
    private List<PassengerViewModel> passenger;
}
