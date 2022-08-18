package com.unosquare.carmigo.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateDriverViewModel {

  @NotEmpty
  @Size(min = 1, max = 100)
  private String licenseNumber;
}
