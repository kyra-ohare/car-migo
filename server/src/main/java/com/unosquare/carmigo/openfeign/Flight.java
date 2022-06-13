package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Flight {

  private String start;

  private String dest;

  private double distance;

  private String time;
}
