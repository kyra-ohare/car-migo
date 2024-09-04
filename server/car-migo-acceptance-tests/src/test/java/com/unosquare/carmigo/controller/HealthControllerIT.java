package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.controller.HealthController.RESPONSE_BODY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class HealthControllerIT {

  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new HealthController()).build();
  }

  @SneakyThrows
  @Test
  void healthTest() {
    final var response = mockMvc.perform(get("/v1/health"))
        .andExpect(status().isOk()).andReturn();
    String content = response.getResponse().getContentAsString();
    assertEquals(content, RESPONSE_BODY);
  }
}
