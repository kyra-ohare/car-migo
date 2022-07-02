package com.unosquare.carmigo.security;

import static com.unosquare.carmigo.constant.AppConstants.ACTIVE;
import static com.unosquare.carmigo.constant.AppConstants.ADMIN;
import static com.unosquare.carmigo.constant.AppConstants.DEV;
import static com.unosquare.carmigo.constant.AppConstants.NOT_PERMITTED;
import static com.unosquare.carmigo.constant.AppConstants.SUSPENDED;

import com.unosquare.carmigo.exception.UnauthorizedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Authorization {

  private AppUser appUser;

  public void verifyUserAuthorization(final int id) {
    final AppUser.Current currentAppUser = appUser.get();
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
                    || whichMethodCalled.equals("patchPlatformUserById"))
            )
        ))
        || userAccess.equals(ADMIN) || userAccess.equals(DEV)
    )) {
      throw new UnauthorizedException(NOT_PERMITTED);
    }
  }
}
