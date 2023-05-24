package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * The property of a point.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PointsProperty {

  private Geocode geocode;
}
