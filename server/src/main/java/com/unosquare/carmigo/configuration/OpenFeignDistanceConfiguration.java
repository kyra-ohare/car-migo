package com.unosquare.carmigo.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Open feign configuration to communicate with <a href="https://www.distance.to/">Distance</a> API.
 */
@Configuration
public class OpenFeignDistanceConfiguration {

  @Value("${open-feign.distance.host}")
  private String rapidApiHost;

  @Value("${open-feign.distance.key}")
  private String rapidApiKey;

  /**
   * Intercepts a request to Distance to add some headers.
   *
   * @return a {@link RequestInterceptor}.
   */
  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      requestTemplate.header("X-RapidAPI-Host", rapidApiHost);
      requestTemplate.header("X-RapidAPI-Key", rapidApiKey);
      requestTemplate.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    };
  }
}
