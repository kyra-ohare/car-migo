package com.unosquare.carmigo.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class CreateJourneyDTO {

  private int locationIdFrom;

  private int locationIdTo;

  private int maxPassengers;

  private Instant dateTime;
}
