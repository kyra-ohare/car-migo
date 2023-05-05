package com.unosquare.carmigo.dto.response;

import lombok.Data;

@Data
public class DistanceResponse {

  private Location locationFrom;

  private Location locationTo;

  private Distance distance;

  @Data
  public static class Location {

    private String location;

    private Coordinate coordinates;
  }

  @Data
  public static class Coordinate {

    private double latitude;

    private double longitude;
  }

  @Data
  public static class Distance {

    private double km;

    private double mi;
  }
}
