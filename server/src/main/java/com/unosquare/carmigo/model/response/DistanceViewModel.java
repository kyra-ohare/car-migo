package com.unosquare.carmigo.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DistanceViewModel {

  private Location locationFrom;

  private Location locationTo;

  private Distance distance;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Location {

    private String location;

    private Coordinate coordinates;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Coordinate {

    private double latitude;

    private double longitude;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class Distance {

    private double km;

    private double mi;
  }
}
