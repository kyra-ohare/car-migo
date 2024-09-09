package com.unosquare.carmigo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.unosquare.carmigo.entity.UserAccessStatus;
import com.unosquare.carmigo.repository.UserAccessStatusRepository;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class HeartbeatControllerIT {

  private static final String ENDPOINT = "/v1/heartbeat";

  private MockMvc mockMvc;
  @Mock private UserAccessStatusRepository userAccessStatusRepositoryMock;
  @Mock private RedisTemplate<String, Object> redisTemplateMock;
  @Mock private RedisConnection redisConnectionMock;
  @Fixture private UserAccessStatus userAccessStatusFixture;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(
        new HeartbeatController(userAccessStatusRepositoryMock, redisTemplateMock))
      .build();

    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);

    when(userAccessStatusRepositoryMock.findById(anyInt())).thenReturn(Optional.of(userAccessStatusFixture));
    RedisConnectionFactory redisConnectionFactory = mock(RedisConnectionFactory.class);
    when(redisTemplateMock.getConnectionFactory()).thenReturn(redisConnectionFactory);
    when(redisConnectionFactory.getConnection()).thenReturn(redisConnectionMock);
    when(redisConnectionMock.ping()).thenReturn("PONG");
  }

  @SneakyThrows
  @Test
  void testSuccessfulHeartbeat() {
    final var result = this.mockMvc.perform(get(ENDPOINT)).andExpect(status().isOk()).andReturn();
    assertEquals(200, result.getResponse().getStatus());
  }

  @SneakyThrows
  @Test
  void testHeartbeatFailsWhenDbIsDown() {
    when(userAccessStatusRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
    final var result = this.mockMvc.perform(get(ENDPOINT)).andExpect(status().isInternalServerError()).andReturn();
    assertEquals(500, result.getResponse().getStatus());
  }

  @SneakyThrows
  @Test
  void testHeartbeatFailsWhenDbThrowsException() {
    when(userAccessStatusRepositoryMock.findById(anyInt())).thenThrow(new NullPointerException());
    final var result = this.mockMvc.perform(get(ENDPOINT)).andExpect(status().isInternalServerError()).andReturn();
    assertEquals(500, result.getResponse().getStatus());
  }

  @SneakyThrows
  @Test
  void testHeartbeatFailsWhenRedisIsDown() {
    when(redisConnectionMock.ping()).thenReturn(null);
    final var result = this.mockMvc.perform(get(ENDPOINT)).andExpect(status().isInternalServerError()).andReturn();
    assertEquals(500, result.getResponse().getStatus());
  }

  @SneakyThrows
  @Test
  void testHeartbeatFailsWhenRedisThrowsException() {
    when(redisConnectionMock.ping()).thenThrow(new NullPointerException());
    final var result = this.mockMvc.perform(get(ENDPOINT)).andExpect(status().isInternalServerError()).andReturn();
    assertEquals(500, result.getResponse().getStatus());
  }
}
