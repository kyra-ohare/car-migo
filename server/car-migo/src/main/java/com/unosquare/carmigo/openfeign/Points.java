package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Representing the distance between point A to point B.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Points {

  private PointsProperty properties;
}
