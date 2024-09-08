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
public class DriverResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 7895580125298236635L;

  private int id;

  private String licenseNumber;

  private PlatformUserResponse platformUser;
}
