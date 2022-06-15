package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Distance {

  private double haversine;

  private double greatCircle;

  private double vincenty;

  private double radians;

  private double dagrees;

  private List<Flight> flight;
}
