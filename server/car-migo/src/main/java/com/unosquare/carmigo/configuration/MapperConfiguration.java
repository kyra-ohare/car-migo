package com.unosquare.carmigo.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * ModelMapper and ObjectMapper configurations.
 */
@Configuration
public class MapperConfiguration {

  /**
   * ModelMapper configuration.
   *
   * @return {@link ModelMapper}
   */
  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public ModelMapper modelMapper() {
    final ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration()
        .setAmbiguityIgnored(true)
        .setMatchingStrategy(MatchingStrategies.STRICT)
        .setPropertyCondition(Conditions.isNotNull());
    return mapper;
  }

  /**
   * ObjectMapper configuration.
   *
   * @return {@link ObjectMapper}
   */
  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  public ObjectMapper objectMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper;
  }
}
