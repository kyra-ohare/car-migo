package com.unosquare.carmigo.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DriverRequest {

  @NotEmpty
  @Size(min = 1, max = 100)
  private String licenseNumber;
}
