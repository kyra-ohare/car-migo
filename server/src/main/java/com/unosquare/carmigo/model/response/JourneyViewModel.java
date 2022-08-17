package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class JourneyViewModel {

  private int id;

  private Instant createdDate;

  private LocationViewModel locationFrom;

  private LocationViewModel locationTo;

  private int maxPassengers;

  private Instant dateTime;

  private DriverViewModel driver;

  private List<PassengerViewModel> passengers;
}
