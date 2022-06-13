package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Geocode {

  private long id;

  private String name;

  @JsonIgnoreProperties("lat")
  private double latitude;

  @JsonIgnoreProperties("lng")
  private double longitude;

  @JsonIgnoreProperties("lang")
  private String language;

  private String country;

  private List<Region> regions;
}
