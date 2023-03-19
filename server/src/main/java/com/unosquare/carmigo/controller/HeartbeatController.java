package com.unosquare.carmigo.controller;

import com.unosquare.carmigo.repository.UserAccessStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/heartbeat")
@Tag(name = "Heartbeat Controller")
public class HeartbeatController {

  private final UserAccessStatus userAccessStatus;

  @GetMapping
  public ResponseEntity<Map<String, Boolean>> heartbeat() {
    final var response = new HashMap<String, Boolean>();
    response.put("Database", canConnectToDb());

    boolean isHealthy = true;
    for (boolean status : response.values()) {
      isHealthy = isHealthy && status;
    }

    if (isHealthy) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private boolean canConnectToDb() {
    try {
      return userAccessStatus.findById(1).isPresent();
    } catch (Exception e) {
      System.err.println("Error occurred during DB health check. " + e.getMessage());
      return false;
    }
  }
}
