package com.unosquare.carmigo.openfeign;

public class DistanceApiFallback implements DistanceApi {

  @Override
  public DistanceHolder getDistance(final String route) {
    return null;
  }
}
