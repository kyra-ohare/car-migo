package com.unosquare.carmigo.security;

import static com.unosquare.carmigo.contant.AppConstants.NO_PERMISSIONS;

import com.unosquare.carmigo.exception.AuthenticationException;
import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class AppUser {

  @Bean
  @RequestScope
  public Current get() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.getPrincipal() instanceof CustomUserDetails) {
      final CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
      return Current.builder()
          .id(customUserDetails.getId())
          .username(customUserDetails.getUsername())
          .userAccessStatus(customUserDetails.getUserAccessStatus())
          .authorities(customUserDetails.getAuthorities())
          .build();
    }
    throw new AuthenticationException(NO_PERMISSIONS);
  }

  @Getter
  @Builder
  public static class Current {

    private int id;
    private String username;
    private String userAccessStatus;
    private Collection<? extends GrantedAuthority> authorities;
  }
}
