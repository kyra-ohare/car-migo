package com.unosquare.carmigo.openfeign;

/**
 * Triggers when there is a problem communicating with <a href="https://www.distance.to/">Distance</a> API.
 */
public class DistanceApiFallback implements DistanceApi {

  @Override
  public DistanceHolder getDistance(final String route) {
    return null;
  }
}
