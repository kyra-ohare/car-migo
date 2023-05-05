package com.unosquare.carmigo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import lombok.Data;

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
