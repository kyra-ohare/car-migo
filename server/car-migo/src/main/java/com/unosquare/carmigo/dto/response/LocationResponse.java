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
public class LocationResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 8224296289499727272L;

  private int id;

  private String description;
}
