package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Geocode {

  private long id;

  private String name;

  @JsonProperty("lat")
  private double latitude;

  @JsonProperty("lng")
  private double longitude;

  @JsonProperty("lang")
  private String language;

  private String country;

  private List<Region> regions;
}
