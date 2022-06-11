package com.unosquare.carmigo.service;

import com.unosquare.carmigo.entity.PlatformUser;
import com.unosquare.carmigo.repository.PlatformUserRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

  private final PlatformUserRepository platformUserRepository;

  @Override
  public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
    final PlatformUser currentUser = platformUserRepository.findPlatformUserByEmail(email);
    return new User(currentUser.getEmail(), currentUser.getPassword(), new ArrayList<>());
  }
}
