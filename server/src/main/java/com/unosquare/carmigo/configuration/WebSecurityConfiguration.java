package com.unosquare.carmigo.configuration;

import static com.unosquare.carmigo.constant.AppConstants.ADMIN;
import static com.unosquare.carmigo.constant.AppConstants.DEV;

import com.unosquare.carmigo.security.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final JwtRequestFilter jwtRequestFilter;

  @Override
  protected void configure(final HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable().authorizeRequests()
        .antMatchers(HttpMethod.POST, "/v1/users/create").permitAll()
        .antMatchers(HttpMethod.POST, "/v1/login").permitAll()
        .antMatchers(HttpMethod.GET, "/v1/journeys/calculateDistance").permitAll()
        .antMatchers(HttpMethod.GET, "/v1/journeys/search").permitAll()
        .antMatchers(HttpMethod.GET, "/actuator/**").hasAnyAuthority(ADMIN, DEV)
        .antMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
        .antMatchers(HttpMethod.GET, "/v3/api-docs**").permitAll()
        .antMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
        .anyRequest().authenticated()
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
