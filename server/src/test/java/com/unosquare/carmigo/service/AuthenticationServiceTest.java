package com.unosquare.carmigo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flextrade.jfixture.FixtureAnnotations;
import com.flextrade.jfixture.JFixture;
import com.flextrade.jfixture.annotations.Fixture;
import com.unosquare.carmigo.dto.request.AuthenticationRequest;
import com.unosquare.carmigo.dto.response.AuthenticationResponse;
import com.unosquare.carmigo.security.UserSecurityService;
import com.unosquare.carmigo.util.JwtTokenService;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

  @Mock private UserSecurityService userSecurityServiceMock;
  @Mock private AuthenticationManager authenticationManagerMock;
  @Mock private JwtTokenService jwtTokenServiceMock;
  @InjectMocks private AuthenticationService authenticationService;

  @Fixture private AuthenticationRequest authenticationRequestFixture;
  @Fixture private AuthenticationResponse authenticationResponseFixture;

  @BeforeEach
  public void setUp() {
    final JFixture jFixture = new JFixture();
    jFixture.customise().circularDependencyBehaviour().omitSpecimen();
    FixtureAnnotations.initFixtures(this, jFixture);
  }

  @Test
  public void create_Authentication_Token_Returns_AuthenticationResponse() {
    final UserDetails spyUserDetails = spy(new User("foo", "foo", new ArrayList<>()));
    when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(any());
    when(userSecurityServiceMock.loadUserByUsername(authenticationRequestFixture.getEmail()))
      .thenReturn(spyUserDetails);
    when(jwtTokenServiceMock.generateToken(spyUserDetails)).thenReturn(anyString());
    final AuthenticationResponse authenticationResponse = authenticationService.createAuthenticationToken(
      authenticationRequestFixture);
    authenticationResponse.setJwt(this.authenticationResponseFixture.getJwt());

    assertThat(authenticationResponse.getJwt()).isEqualTo(this.authenticationResponseFixture.getJwt());
    verify(authenticationManagerMock).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(userSecurityServiceMock).loadUserByUsername(anyString());
    verify(jwtTokenServiceMock).generateToken(any(UserDetails.class));
  }
}
