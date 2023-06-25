package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Distance API returns an array of Steps. However, the focus is on distance.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Steps {

  private Distance distance;
}
