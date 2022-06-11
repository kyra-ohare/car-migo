package com.unosquare.carmigo.configuration;

import static com.unosquare.carmigo.contant.AppConstants.ADMIN;
import static com.unosquare.carmigo.contant.AppConstants.DEV;

import com.unosquare.carmigo.security.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final JwtRequestFilter jwtRequestFilter;

  @Override
  protected void configure(final HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable().authorizeRequests()
        .antMatchers(HttpMethod.POST, "/v1/users").permitAll()
        .antMatchers(HttpMethod.POST, "/v1/users/authenticate").permitAll()
        .antMatchers(HttpMethod.GET, "/v1/journeys/search").permitAll()
        .antMatchers(HttpMethod.GET, "/actuator/info").hasAuthority(DEV)
        .antMatchers(HttpMethod.GET, "/actuator/health").hasAnyAuthority(ADMIN, DEV)
        .antMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
        .antMatchers(HttpMethod.GET, "/v3/api-docs/swagger-config").permitAll()
        .antMatchers(HttpMethod.GET, "/v3/api-docs**").permitAll()
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
