package com.unosquare.carmigo.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

@TestConfiguration
public class TestRedisConfiguration {

  private final RedisServer redisServer;

  public TestRedisConfiguration(RedisProperties redisProperties) throws IOException {
    this.redisServer = new RedisServer(redisProperties.getPort());
  }

  @PostConstruct
  public void postConstruct() throws IOException {
    redisServer.start();
  }

  @PreDestroy
  public void preDestroy() throws IOException {
    redisServer.stop();
  }
}
