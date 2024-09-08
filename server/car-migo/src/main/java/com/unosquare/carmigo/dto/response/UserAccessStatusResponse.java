package com.unosquare.carmigo.dto.response;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object from the Service back to Controller.
 */
@NoArgsConstructor
@Getter
@Setter
public class UserAccessStatusResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = -7565836506506145530L;
  private int id;

  private String status;
}
