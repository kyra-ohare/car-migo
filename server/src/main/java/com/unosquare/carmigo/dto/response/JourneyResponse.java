package com.unosquare.carmigo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object from the Service back to Controller.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class JourneyResponse {

  private int id;

  private Instant createdDate;

  private LocationResponse locationFrom;

  private LocationResponse locationTo;

  private int maxPassengers;

  private Instant dateTime;

  private DriverResponse driver;

  private List<PassengerResponse> passengers;
}
