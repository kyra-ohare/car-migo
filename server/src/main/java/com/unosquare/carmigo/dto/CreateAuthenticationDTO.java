package com.unosquare.carmigo.dto;

import lombok.Data;

@Data
public class CreateAuthenticationDTO {

  private String email;

  private String password;
}
