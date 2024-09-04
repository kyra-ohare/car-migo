package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.controller.HealthController.RESPONSE_BODY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unosquare.carmigo.util.AuthenticationBeansTestCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HealthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthenticationBeansTestCase.class)
class HealthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @SneakyThrows
  @Test
  void healthTest() {
    final var response = mockMvc.perform(get("/v1/health"))
        .andExpect(status().isOk()).andReturn();
    String content = response.getResponse().getContentAsString();
    assertEquals(content, RESPONSE_BODY);
  }
}
