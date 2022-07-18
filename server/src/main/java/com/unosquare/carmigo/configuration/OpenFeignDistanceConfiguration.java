package com.unosquare.carmigo.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
public class OpenFeignDistanceConfiguration {

  @Value("${open-feign.distance.host}")
  private String rapidApiHost;

  @Value("${open-feign.distance.key}")
  private String rapidApiKey;

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      requestTemplate.header("X-RapidAPI-Host", rapidApiHost);
      requestTemplate.header("X-RapidAPI-Key", rapidApiKey);
      requestTemplate.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    };
  }
}
