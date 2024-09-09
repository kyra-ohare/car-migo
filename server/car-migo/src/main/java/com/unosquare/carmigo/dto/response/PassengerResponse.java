package com.unosquare.carmigo.dto.response;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object from the Service back to Controller.
 */
@Getter
@Setter
@NoArgsConstructor
public class PassengerResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = -5388142230072962195L;

  private int id;

  private PlatformUserResponse platformUser;
}
