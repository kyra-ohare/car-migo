package com.unosquare.carmigo.openfeign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Region {

  private long id;

  @JsonProperty("name")
  private String nameInLocalLanguage;

  @JsonProperty("name_en")
  private String nameInEnglish;

  private long level;
}
