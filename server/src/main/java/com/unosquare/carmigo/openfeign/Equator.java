package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Equator {

  private double distance;

  private String bearing;
}
