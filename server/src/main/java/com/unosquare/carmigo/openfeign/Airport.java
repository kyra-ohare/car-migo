package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Airport {

  private String name;
  private String icao; // International Civil Aviation Organization
  private String iata; // International Air Transport Association
  private String city;
  private String country;
  private long distance;

  @JsonProperty("lat")
  private long latitude;

  @JsonProperty("lng")
  private long longitude;
}
