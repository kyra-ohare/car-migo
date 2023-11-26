package com.unosquare.carmigo.configuration;

import static com.unosquare.carmigo.security.UserStatus.ADMIN;
import static com.unosquare.carmigo.security.UserStatus.DEV;

import com.unosquare.carmigo.security.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Web security configuration.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

  private final JwtRequestFilter jwtRequestFilter;

  /**
   * Security layer filter which allows some endpoints depending on the authority.
   * Endpoints not listed below are restricted by default.
   *
   * @param httpSecurity HTTP web based security.
   * @return a {@link SecurityFilterChain} which is passed to next Spring filter.
   * @throws Exception application halts if it does not go according to the plan.
   */
  @Bean
  protected SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .authorizeHttpRequests(
            authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/v1/heartbeat").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/users/create").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/users/confirm-email").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/journeys/calculateDistance").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/journeys/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/**").hasAnyAuthority(ADMIN.name(), DEV.name())
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/v3/api-docs**").permitAll()
                .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
        )
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsFilter()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  protected AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  protected BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  protected CorsConfigurationSource corsFilter() {
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("http://localhost:8087");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }
}
