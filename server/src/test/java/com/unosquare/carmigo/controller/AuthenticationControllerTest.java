package com.unosquare.carmigo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unosquare.carmigo.service.PlatformUserService;
import com.unosquare.carmigo.util.ResourceUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

  private static final String API_ENDPOINT = "/v1/login";
  private static final String POST_AUTHENTICATION_VALID_JSON =
      ResourceUtility.generateStringFromResource("requestJson/PostAuthenticationValid.json");
  private static final String POST_AUTHENTICATION_INVALID_JSON =
      ResourceUtility.generateStringFromResource("requestJson/PostAuthenticationInvalid.json");

  private MockMvc mockMvc;

  @Mock private ModelMapper modelMapperMock;
  @Mock private PlatformUserService platformUserServiceMock;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(
            new AuthenticationController(modelMapperMock, platformUserServiceMock))
        .build();
  }

  @Test
  public void post_Create_Authentication_Token_Returns_HttpStatus_Created() throws Exception {
    mockMvc.perform(post(API_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(POST_AUTHENTICATION_VALID_JSON))
        .andExpect(status().isCreated());
    verify(platformUserServiceMock).createAuthenticationToken(any());
  }

  @Test
  public void post_Create_Authentication_Token_Returns_HttpStatus_BadRequest() throws Exception {
    mockMvc.perform(post(API_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(POST_AUTHENTICATION_INVALID_JSON))
        .andExpect(status().isBadRequest());
    verify(platformUserServiceMock, times(0)).createAuthenticationToken(any());
  }
}
