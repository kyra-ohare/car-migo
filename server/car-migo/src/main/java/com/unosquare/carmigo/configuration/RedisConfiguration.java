package com.unosquare.carmigo.configuration;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * Redis Cache Configuration.
 */
@Component
@Configuration
@EnableCaching
@Setter
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfiguration {

  @Value(value = "${redis.hostname}")
  private String hostname;

  @Value(value = "${redis.port}")
  private int redisPort;

  @Value(value = "${redis.connection_timeout}")
  private long connectionTimeout;

  @Value(value = "${redis.command_timeout}")
  private long commandTimeout;

  /**
   * Redis Template.
   */
  @Bean(name = "redisTemplate")
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(lettuceConnectionFactory());
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }

  /**
   * Redis Lettuce connection factory.
   */
  @Bean
  LettuceConnectionFactory lettuceConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
        hostname, redisPort);

    final SocketOptions socketOptions =
        SocketOptions.builder().connectTimeout(Duration.ofMillis(connectionTimeout)).build();
    final ClientOptions clientOptions =
        ClientOptions.builder().socketOptions(socketOptions).build();

    final var lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder()
        .clientOptions(clientOptions)
        .commandTimeout(Duration.ofMillis(commandTimeout));

    final LettuceClientConfiguration clientConfig = lettuceClientConfigurationBuilder.build();

    return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
  }
}
