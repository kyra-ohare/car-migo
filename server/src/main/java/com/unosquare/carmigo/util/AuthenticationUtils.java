package com.unosquare.carmigo.util;

import static com.unosquare.carmigo.constant.AppConstants.ACTIVE;
import static com.unosquare.carmigo.constant.AppConstants.ADMIN;
import static com.unosquare.carmigo.constant.AppConstants.DEV;
import static com.unosquare.carmigo.constant.AppConstants.NO_PERMISSIONS;
import static com.unosquare.carmigo.constant.AppConstants.SUSPENDED;

import com.unosquare.carmigo.exception.AuthenticationException;
import com.unosquare.carmigo.security.AppUser;

public class AuthenticationUtils {

  public static void verifyUserPermission(final int id, final AppUser.Current currentAppUser) {
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

  private AuthenticationUtils() {}
}
