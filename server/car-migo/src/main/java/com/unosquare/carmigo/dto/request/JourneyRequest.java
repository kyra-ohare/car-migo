package com.unosquare.carmigo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import lombok.Data;

/**
 * Data Transfer Object received by the Controller to the Service.
 * Represents the requirements to create a journey.
 */
@Data
public class JourneyRequest {

  @Positive
  private int locationIdFrom;

  @Positive
  private int locationIdTo;

  @Positive
  private int maxPassengers;

  @NotNull
  private Instant dateTime;
}
