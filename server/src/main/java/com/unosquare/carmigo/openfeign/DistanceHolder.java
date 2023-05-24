package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds the data fetched from <a href="https://www.distance.to/">Distance</a> API.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistanceHolder {

  private List<Points> points;

  private List<Steps> steps;
}
