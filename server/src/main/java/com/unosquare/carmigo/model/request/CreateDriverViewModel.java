package com.unosquare.carmigo.model.request;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateDriverViewModel {

  @Size(min = 1, max = 100)
  private String licenseNumber;
}
