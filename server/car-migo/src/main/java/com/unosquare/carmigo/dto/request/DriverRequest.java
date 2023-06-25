package com.unosquare.carmigo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object received by the Controller to the Service.
 * Represents the requirements to create a driver.
 */
@Data
public class DriverRequest {

  @NotEmpty
  @Size(min = 1, max = 100)
  private String licenseNumber;
}
