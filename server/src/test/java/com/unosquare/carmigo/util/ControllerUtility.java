package com.unosquare.carmigo.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

public class ControllerUtility {

  private final MockMvc mockMvc;
  private final String apiLeading;

  public ControllerUtility(final MockMvc mockMvc, final String apiLeading) {
    this.mockMvc = mockMvc;
    this.apiLeading = apiLeading;
  }

  public void makeGetRequest(final String apiTrailing, final ResultMatcher expectation) throws Exception {
    mockMvc.perform(get(apiLeading + apiTrailing)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(expectation);
  }

  public void makePostRequest(final String apiTrailing, final String content, final ResultMatcher expectation)
      throws Exception {
    mockMvc.perform(post(apiLeading + apiTrailing)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(content))
        .andExpect(expectation);
  }

  public void makePostRequestWithOneParam(final String apiTrailing, final String paramKey, final String paramValue,
      final ResultMatcher expectation) throws Exception {
    mockMvc.perform(post(apiLeading + apiTrailing)
            .contentType(MediaType.APPLICATION_JSON_VALUE).param(paramKey, paramValue))
        .andExpect(expectation);
  }

  public void makePatchRequest(final String apiTrailing, final String content, final ResultMatcher expectation)
      throws Exception {
    mockMvc.perform(patch(apiLeading + apiTrailing)
            .contentType("application/json-patch+json")
            .content(content))
        .andExpect(expectation);
  }

  public void makeDeleteRequest(final String apiTrailing, final ResultMatcher expectation) throws Exception {
    mockMvc.perform(delete(apiLeading + apiTrailing))
        .andExpect(expectation);
  }
}
