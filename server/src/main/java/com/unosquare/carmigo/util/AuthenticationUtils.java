package com.unosquare.carmigo.util;

import com.unosquare.carmigo.exception.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import static com.unosquare.carmigo.contant.AppContants.ACTIVE;
import static com.unosquare.carmigo.contant.AppContants.ADMIN;
import static com.unosquare.carmigo.contant.AppContants.DEV;
import static com.unosquare.carmigo.contant.AppContants.NO_PERMISSIONS;

public class AuthenticationUtils
{
    private AuthenticationUtils() {}

    public static void verifyUserPermission(final String username, final Authentication authentication)
    {
        String userAccess = "";
        for (final GrantedAuthority item : authentication.getAuthorities()) {
            userAccess = item.getAuthority();
        }
        if (!(authentication.isAuthenticated()
                && (username.equals(authentication.getName())
                && (userAccess.equals(ACTIVE) || userAccess.equals(ADMIN) || userAccess.equals(DEV))))) {
            throw new AuthenticationException(NO_PERMISSIONS);
        }
    }
}
