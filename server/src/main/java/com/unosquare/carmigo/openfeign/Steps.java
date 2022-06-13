package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Steps {
  private List<Double> start;

  private List<Double> end;

  private Distance distance;

  private Midpoint midpoint;
}
