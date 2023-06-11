package com.unosquare.carmigo.controller;

import static com.unosquare.carmigo.util.ResourceUtility.convertObjectToJsonBytes;
import static com.unosquare.carmigo.util.ResourceUtility.getObjectResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.unosquare.carmigo.dto.request.AuthenticationRequest;
import com.unosquare.carmigo.dto.response.AuthenticationResponse;
import com.unosquare.carmigo.service.AuthenticationService;
import com.unosquare.carmigo.util.AuthenticationBeansTestCase;
import java.util.LinkedHashMap;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthenticationBeansTestCase.class)
public class AuthenticationControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private AuthenticationService authenticationServiceMock;
  @Fixture private AuthenticationRequest authenticationRequestFixture;
  @Fixture private AuthenticationResponse authenticationResponseFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);
  }

  @SneakyThrows
  @Test
  void createAuthenticationTokenTest() {
    when(authenticationServiceMock.createAuthenticationToken(any(AuthenticationRequest.class))).thenReturn(
        authenticationResponseFixture);
    final var response = mockMvc.perform(post("/v1/login").contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(convertObjectToJsonBytes(authenticationRequestFixture))).andExpect(status().isCreated()).andReturn();

    final LinkedHashMap<String, Object> content = getObjectResponse(response.getResponse().getContentAsString());
    assertEquals(content.get("jwt"), authenticationResponseFixture.getJwt(), "JWTs do not match");
    verify(authenticationServiceMock).createAuthenticationToken(any(AuthenticationRequest.class));
  }
}
