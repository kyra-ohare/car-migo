package com.unosquare.carmigo.service;

import com.unosquare.carmigo.model.request.AuthenticationRequest;
import com.unosquare.carmigo.model.response.AuthenticationResponse;
import com.unosquare.carmigo.security.UserSecurityService;
import com.unosquare.carmigo.util.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserSecurityService userSecurityService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;

  public AuthenticationResponse createAuthenticationToken(final AuthenticationRequest authenticationRequest) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
      authenticationRequest.getEmail(), authenticationRequest.getPassword()));
    final UserDetails userDetails = userSecurityService.loadUserByUsername(authenticationRequest.getEmail());
    final String jwt = jwtTokenService.generateToken(userDetails);
    final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    authenticationResponse.setJwt(jwt);
    return authenticationResponse;
  }
}
