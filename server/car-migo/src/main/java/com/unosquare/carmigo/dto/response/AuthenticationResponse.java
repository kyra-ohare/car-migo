package com.unosquare.carmigo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object from the Service back to Controller.
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthenticationResponse {

  private String jwt;
}
