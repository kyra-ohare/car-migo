package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.util.ResourceUtility.getObjectResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.unosquare.carmigo.entity.UserAccessStatus;
import com.unosquare.carmigo.repository.UserAccessStatusRepository;
import com.unosquare.carmigo.util.AuthenticationBeansTestCase;
import java.util.LinkedHashMap;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HeartbeatController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthenticationBeansTestCase.class)
public class HeartbeatControllerTest {

  private static final String API = "/v1/heartbeat";

  @Autowired private MockMvc mockMvc;
  @MockBean private UserAccessStatusRepository userAccessStatusRepositoryMock;
  @MockBean private RedisTemplate<String, Object> redisTemplateMock;
  @Mock private RedisConnection redisConnectionMock;
  @Fixture private UserAccessStatus userAccessStatusFixture;


  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);

    when(userAccessStatusRepositoryMock.findById(anyInt())).thenReturn(Optional.ofNullable(userAccessStatusFixture));
    RedisConnectionFactory redisConnectionFactory = mock(RedisConnectionFactory.class);
    when(redisTemplateMock.getConnectionFactory()).thenReturn(redisConnectionFactory);
    when(redisConnectionFactory.getConnection()).thenReturn(redisConnectionMock);
    when(redisConnectionMock.ping()).thenReturn("PONG");
  }

  @SneakyThrows
  @Test
  void heartbeatTest() {
    final var response = mockMvc.perform(get(API))
        .andExpect(status().isOk()).andReturn();

    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("Is Database running?"), true);
    assertEquals(content.get("Is Redis cache running?"), true);
    verify(userAccessStatusRepositoryMock).findById(anyInt());
    verify(redisTemplateMock).getConnectionFactory();
  }

  @SneakyThrows
  @Test
  void testDownDatabase() {
    when(userAccessStatusRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
    final var response = mockMvc.perform(get(API))
        .andExpect(status().isInternalServerError()).andReturn();

    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("Is Database running?"), false);
    assertEquals(content.get("Is Redis cache running?"), true);
    verify(userAccessStatusRepositoryMock).findById(anyInt());
    verify(redisTemplateMock).getConnectionFactory();
  }

  @SneakyThrows
  @Test
  void testDownRedis() {
    when(redisConnectionMock.ping()).thenReturn(null);
    final var response = mockMvc.perform(get(API))
        .andExpect(status().isInternalServerError()).andReturn();

    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("Is Database running?"), true);
    assertEquals(content.get("Is Redis cache running?"), false);
    verify(userAccessStatusRepositoryMock).findById(anyInt());
    verify(redisTemplateMock).getConnectionFactory();
  }
}
