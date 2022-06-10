package com.unosquare.carmigo.util;

import com.unosquare.carmigo.exception.AuthenticationException;
import com.unosquare.carmigo.security.AppUser;

import static com.unosquare.carmigo.contant.AppConstants.ACTIVE;
import static com.unosquare.carmigo.contant.AppConstants.ADMIN;
import static com.unosquare.carmigo.contant.AppConstants.DEV;
import static com.unosquare.carmigo.contant.AppConstants.NO_PERMISSIONS;
import static com.unosquare.carmigo.contant.AppConstants.SUSPENDED;

public class AuthenticationUtils
{
    private AuthenticationUtils() {}

    public static void verifyUserPermission(final int id, final AppUser.Current currentAppUser)
    {
        final String whichMethodCalled = StackWalker.getInstance().walk(
                        stream -> stream.skip(3).findFirst().orElseThrow())
                .getMethodName();
        final String userAccess = currentAppUser.getUserAccessStatus();
        if (!((id == currentAppUser.getId()
                &&
                (userAccess.equals(ACTIVE)
                        ||
                        (userAccess.equals(SUSPENDED)
                                &&
                                (whichMethodCalled.equals("getPlatformUserById")
                                        || whichMethodCalled.equals("patchPlatformUser"))
                        )
                ))
                || userAccess.equals(ADMIN) || userAccess.equals(DEV)
        )) {
            throw new AuthenticationException(NO_PERMISSIONS);
        }
    }
}
