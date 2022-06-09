package com.unosquare.carmigo.util;

import com.unosquare.carmigo.exception.AuthenticationException;
import com.unosquare.carmigo.security.SiteUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.unosquare.carmigo.contant.AppConstants.ACTIVE;
import static com.unosquare.carmigo.contant.AppConstants.ADMIN;
import static com.unosquare.carmigo.contant.AppConstants.DEV;
import static com.unosquare.carmigo.contant.AppConstants.NO_PERMISSIONS;

public class AuthenticationUtils
{
    private AuthenticationUtils() {}

    public static void verifyUserPermission(final String username, final Authentication authentication)
    {
        String userAccess = ((SiteUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserAccessStatus();
        if (!(authentication.isAuthenticated()
                && (username.equals(authentication.getName())
                && (userAccess.equals(ACTIVE) || userAccess.equals(ADMIN) || userAccess.equals(DEV))))) {
            throw new AuthenticationException(NO_PERMISSIONS);
        }
    }
}
