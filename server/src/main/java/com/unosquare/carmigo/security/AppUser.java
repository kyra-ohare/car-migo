package com.unosquare.carmigo.security;

import static com.unosquare.carmigo.constant.AppConstants.NOT_PERMITTED;

import com.unosquare.carmigo.exception.UnauthorizedException;
import java.util.ArrayList;
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
          .userAccessStatus(getUserAccessStatus(customUserDetails))
          .build();
    }
    throw new UnauthorizedException(NOT_PERMITTED);
  }

  @Getter
  @Builder
  public static class Current {

    private int id;
    private String username;
    private String userAccessStatus;
  }

  private String getUserAccessStatus(final CustomUserDetails currentAppUser) {
    final ArrayList<GrantedAuthority> authorities = new ArrayList<>(currentAppUser.getAuthorities());
    if (authorities.get(0).getAuthority() == null) {
      throw new UnauthorizedException(NOT_PERMITTED);
    }
    return authorities.get(0).getAuthority();
  }
}
