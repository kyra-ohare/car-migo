package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JourneyDriverViewModel {

  @JsonProperty("id")
  private int id;

  @JsonProperty("createdDate")
  private Instant createdDate;

  @JsonProperty("locationFrom")
  private LocationViewModel locationFrom;

  @JsonProperty("locationTo")
  private LocationViewModel locationTo;

  @JsonProperty("maxPassengers")
  private int maxPassengers;

  @JsonProperty("dateTime")
  private Instant dateTime;

  @JsonProperty("driver")
  private DriverViewModel driver;
}
