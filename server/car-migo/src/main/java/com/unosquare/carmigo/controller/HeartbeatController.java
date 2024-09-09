package com.unosquare.carmigo.controller;

import com.unosquare.carmigo.repository.UserAccessStatusRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles Heartbeat API.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/heartbeat")
@Tag(name = "Heartbeat Controller")
public class HeartbeatController {

  private final UserAccessStatusRepository userAccessStatusRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  /**
   * Verifies if services, this application is depended on, are up and running.<br>
   * Expect 200 OK otherwise 500 Internal Server Error.
   * @return JSON listing the services.
   */
  @GetMapping
  public ResponseEntity<Map<String, Boolean>> heartbeat() {
    final var response = new HashMap<String, Boolean>();
    response.put("Is Database running?", canConnectToDb());
    response.put("Is Redis cache running?", canConnectToRedis());

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
      return userAccessStatusRepository.findById(1).isPresent();
    } catch (Exception e) {
      log.error("Error occurred during DB health check. {}", e.getMessage());
      return false;
    }
  }

  private boolean canConnectToRedis() {
    try {
      String ping = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().ping();
      return StringUtils.isNotEmpty(ping);
    } catch (Exception e) {
      log.error("Error occurred during Redis Cache health check. {}", e.getMessage());
      return false;
    }
  }
}
