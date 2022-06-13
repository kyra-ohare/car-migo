package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Airport {

  private String name;

  private String icao; // International Civil Aviation Organization

  private String iata; // International Air Transport Association

  @JsonProperty("lat")
  private double latitude;

  @JsonProperty("lng")
  private double longitude;

  private String city;

  private String country;

  private double distance;
}
