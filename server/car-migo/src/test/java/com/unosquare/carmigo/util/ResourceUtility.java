package com.unosquare.carmigo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;

public final class ResourceUtility {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private ResourceUtility() {}

  public static String generateStringFromResource(final String path) {
    try {
      return Resources.toString(Resources.getResource(path), Charsets.UTF_8);
    } catch (IOException ex) {
      return "Cannot retrieve resource entity";
    }
  }

  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    return getCustomisedObjectMapper().writeValueAsBytes(object);
  }

  public static <T> T getObjectResponse(String jsonString) {
    try {
      return getCustomisedObjectMapper().readValue(jsonString, new TypeReference<>() {});
    } catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static ObjectMapper getCustomisedObjectMapper() {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }
}
