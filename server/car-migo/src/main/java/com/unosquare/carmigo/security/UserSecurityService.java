package com.unosquare.carmigo.security;

import static com.unosquare.carmigo.constant.AppConstants.NOT_PERMITTED;

import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.exception.UnauthorizedException;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Loads the current user information.
 */
@Service
@AllArgsConstructor
public class UserSecurityService implements UserDetailsService {

  private final PlatformUserRepository platformUserRepository;

  @Override
  public UserDetails loadUserByUsername(final String email) {
    final Optional<PlatformUser> currentUser = platformUserRepository.findPlatformUserByEmail(email);
    if (currentUser.isPresent()) {
      return getUserDetails(currentUser.get());
    }
    throw new UnauthorizedException("Incorrect email (%s) and/or password.".formatted(email));
  }

  /**
   * Returns the user along with their type of access which can be:<br>
   * <br>
   * * ACTIVE - user can use the application without restrictions.<br>
   * * ADMIN - user has admin privileges such as see other users' information.<br>
   * * LOCKED_OUT - use is locked out after 5 failed attempts.<br>
   * * STAGED - account has been created but no email verification yet.<br>
   * * SUSPENDED - user can see and update profile. User cannot create/apply for journeys, accept/reject passengers.
   *
   * @param currentUser PlatformUser from the database.
   * @return the authenticated user's information as{@link UserDetails}.
   */
  private UserDetails getUserDetails(final PlatformUser currentUser) {
    return switch (UserStatus.valueOf(currentUser.getUserAccessStatus().getStatus())) {
      case ACTIVE, ADMIN, DEV, SUSPENDED -> new CustomUserDetails(
        currentUser.getId(), currentUser.getEmail(), currentUser.getPassword(), getAuthorities(currentUser));
      case LOCKED_OUT -> throw new UnauthorizedException("User is locked out after 5 failed attempts.");
      case STAGED -> throw new UnauthorizedException("User needs to confirm email.");
      default -> throw new UnauthorizedException(NOT_PERMITTED);
    };
  }

  private Collection<? extends GrantedAuthority> getAuthorities(final PlatformUser currentUser) {
    return List.of(new SimpleGrantedAuthority(currentUser.getUserAccessStatus().getStatus()));
  }
}
