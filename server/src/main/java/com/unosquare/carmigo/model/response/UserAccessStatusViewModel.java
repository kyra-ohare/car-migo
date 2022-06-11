package com.unosquare.carmigo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserAccessStatusViewModel {

  @JsonProperty("id")
  private int id;

  @JsonProperty("status")
  private String status;
}
