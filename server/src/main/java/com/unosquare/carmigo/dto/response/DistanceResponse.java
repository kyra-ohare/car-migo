package com.unosquare.carmigo.dto.response;

import lombok.Data;

/**
 * Data Transfer Object from the Service back to Controller.
 */
@Data
public class DistanceResponse {

  private Location locationFrom;

  private Location locationTo;

  private Distance distance;

  /**
   * Location of a {@link DistanceResponse}.
   */
  @Data
  public static class Location {

    private String location;

    private Coordinate coordinates;
  }

  /**
   * Coordination of a {@link Location}.
   */
  @Data
  public static class Coordinate {

    private double latitude;

    private double longitude;
  }

  /**
   * Types of distance of a {@link DistanceResponse}.
   */
  @Data
  public static class Distance {

    private double km;

    private double mi;
  }
}
