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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Web security configuration.
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
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
  public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable()
        .authorizeHttpRequests()
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
        .and().cors()
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();

    config.setAllowCredentials(true);
    config.addAllowedOrigin("http://localhost:5173");
    config.addAllowedHeader("*");
    config.addAllowedMethod("GET");
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }
}
