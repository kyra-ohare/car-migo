package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.util.ResourceUtility.getObjectResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HeartbeatController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthenticationBeansTestCase.class)
public class HeartbeatControllerTest {

  private static final String API = "/v1/heartbeat";

  @Autowired private MockMvc mockMvc;
  @MockBean private UserAccessStatusRepository userAccessStatusRepositoryMock;
  @Fixture private UserAccessStatus userAccessStatusFixture;


  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);
  }

  @SneakyThrows
  @Test
  void heartbeatTest() {
    when(userAccessStatusRepositoryMock.findById(anyInt())).thenReturn(Optional.ofNullable(userAccessStatusFixture));
    final var response = mockMvc.perform(get(API))
        .andExpect(status().isOk()).andReturn();

    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("Is Database running?"), true);
    verify(userAccessStatusRepositoryMock).findById(anyInt());
  }

  @SneakyThrows
  @Test
  void heartbeatBadTest() {
    when(userAccessStatusRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
    final var response = mockMvc.perform(get(API))
        .andExpect(status().isInternalServerError()).andReturn();

    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("Is Database running?"), false);
    verify(userAccessStatusRepositoryMock).findById(anyInt());
  }
}
