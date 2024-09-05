package com.unosquare.carmigo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles Health API.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/health")
@Tag(name = "Health Controller")
public class HealthController {

  static final String RESPONSE_BODY = "Car-Migo Server is healthy!";

  /**
   * Healthcheck method. Returns 200 if the microservice is running.
   *
   * @return health check HTTP status.
   */
  @GetMapping
  public ResponseEntity<String> health() {
    return new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK);
  }
}
