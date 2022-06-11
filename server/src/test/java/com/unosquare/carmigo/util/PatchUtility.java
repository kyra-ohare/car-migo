package com.unosquare.carmigo.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.unosquare.carmigo.configuration.MapperConfiguration;

public class PatchUtility {

  private static final ObjectMapper objectMapper = new MapperConfiguration().objectMapper();

  public static JsonNode jsonNode(final Object entity, final JsonPatch jsonPatch) throws Exception {
    return jsonPatch.apply(objectMapper.convertValue(entity, JsonNode.class));
  }

  public static JsonPatch jsonPatch(final String json) throws Exception {
    return JsonPatch.fromJson(objectMapper.readTree(json));
  }

  private PatchUtility() {}
}
