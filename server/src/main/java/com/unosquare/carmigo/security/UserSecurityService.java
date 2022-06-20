package com.unosquare.carmigo.security;

import static com.unosquare.carmigo.constant.AppConstants.ACTIVE;
import static com.unosquare.carmigo.constant.AppConstants.ADMIN;
import static com.unosquare.carmigo.constant.AppConstants.DEV;
import static com.unosquare.carmigo.constant.AppConstants.LOCKED_OUT;
import static com.unosquare.carmigo.constant.AppConstants.STAGED;
import static com.unosquare.carmigo.constant.AppConstants.SUSPENDED;

import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.exception.AuthenticationException;
import com.unosquare.carmigo.exception.ResourceNotFoundException;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

  private final PlatformUserRepository platformUserRepository;

  @Override
  public UserDetails loadUserByUsername(final String email) {
    final Optional<PlatformUser> currentUser = platformUserRepository.findPlatformUserByEmail(email);
    if (currentUser.isPresent()) {
      return getUserDetails(currentUser.get());
    }
    throw new ResourceNotFoundException(String.format("Incorrect email (%s) and/or password", email));
  }

  /**
   * Returns the user along with their type of access which can be:<br><br>
   *
   * * ACTIVE - user can use the application without restrictions.<br>
   * * ADMIN - user has admin privileges such as see other users' information.<br>
   * * LOCKED_OUT - use is locked out after 5 failed attempts.<br>
   * * STAGED - account has been created but no email verification yet.<br>
   * * SUSPENDED - user can see and update profile. User cannot create/apply for journeys, accept/reject passengers.
   *
   * @param currentUser PlatformUser from the database
   * @return the UserDetails
   */
  private UserDetails getUserDetails(final PlatformUser currentUser) {
    switch (currentUser.getUserAccessStatus().getStatus()) {
      case ACTIVE:
      case ADMIN:
      case DEV:
      case SUSPENDED:
        return new CustomUserDetails(currentUser.getId(), currentUser.getEmail(), currentUser.getPassword(),
            currentUser.getUserAccessStatus().getStatus(), List.of());
      case LOCKED_OUT:
        throw new AuthenticationException("User is locked out after 5 failed attempts.");
      case STAGED:
        throw new AuthenticationException("User needs to confirm the email.");
      default:
        throw new NoResultException("Unknown UserAccessStatus property.");
    }
  }
}
