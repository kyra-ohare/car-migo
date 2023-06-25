package com.unosquare.carmigo.util;

import com.unosquare.carmigo.security.UserSecurityService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Create necessary beans to bypass authentication.
 */
@TestConfiguration
@ExtendWith(MockitoExtension.class)
public class AuthenticationBeansTestCase {

  @MockBean UserSecurityService userSecurityServiceMock;
  @MockBean JwtTokenService jwtTokenServiceMock;
}
