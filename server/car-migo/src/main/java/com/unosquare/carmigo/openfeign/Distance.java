package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Distance has many keys. However, the focus is on great circle.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Distance {

  private double greatCircle;
}
