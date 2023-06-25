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

/**
 * Represents a logged-in user.
 */
@Configuration
public class AppUser {

  /**
   * Returns the current logged-in user.
   *
   * @return the logged-in user wrapped in {@link CurrentAppUser}.
   */
  @Bean
  @RequestScope
  public CurrentAppUser get() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
      return CurrentAppUser.builder()
          .id(customUserDetails.getId())
          .username(customUserDetails.getUsername())
          .userAccessStatus(getUserAccessStatus(customUserDetails))
          .build();
    }
    throw new UnauthorizedException(NOT_PERMITTED);
  }

  /**
   * The current logged-in user.
   */
  @Getter
  @Builder
  public static class CurrentAppUser {

    private int id;
    private String username;
    private UserStatus userAccessStatus;
  }

  private UserStatus getUserAccessStatus(final CustomUserDetails currentAppUser) {
    final ArrayList<GrantedAuthority> authorities = new ArrayList<>(currentAppUser.getAuthorities());
    if (authorities.get(0).getAuthority() == null) {
      throw new UnauthorizedException(NOT_PERMITTED);
    }
    return UserStatus.valueOf(authorities.get(0).getAuthority());
  }
}
