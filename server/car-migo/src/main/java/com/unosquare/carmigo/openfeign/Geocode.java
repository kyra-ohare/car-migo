package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * The geocode of a point with its latitude and longitude.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Geocode {

  private String name;

  @JsonProperty("lat")
  private double latitude;

  @JsonProperty("lng")
  private double longitude;
}
