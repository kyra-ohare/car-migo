package com.unosquare.carmigo.security;

import java.io.Serial;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Gives specific characteristics which represents the authenticated user.
 */
@Getter
@Setter
public class CustomUserDetails extends User {

  @Serial
  private static final long serialVersionUID = 7027723211089349611L;

  private int id;

  public CustomUserDetails(final int id, final String username, final String password,
      final Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.id = id;
  }
}
