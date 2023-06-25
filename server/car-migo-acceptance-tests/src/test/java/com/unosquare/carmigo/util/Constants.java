package com.unosquare.carmigo.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;

public final class Constants {

  public static final String STAGED_USER = "staged@example.com";
  public static final String ACTIVE_USER = "active@example.com";
  public static final String SUSPENDED_USER = "suspended@example.com";
  public static final String LOCKED_OUT_USER = "locked_out@example.com";
  public static final String ADMIN_USER = "admin@example.com";

  public static int STAGED_USER_ID = 1;
  public static int ACTIVE_USER_ID = 2;
  public static int SUSPENDED_USER_ID = 3;
  public static int LOCKED_OUT_USER_ID = 4;
  public static int ADMIN_USER_ID = 5;

  public static final String POST_AUTHENTICATION_VALID_JSON =
      generateStringFromResource("jsonAssets/PostAuthenticationValid.json");
  public static final String POST_AUTHENTICATION_INVALID_JSON =
      generateStringFromResource("jsonAssets/PostAuthenticationInvalid.json");

  public static final String POST_PLATFORM_USER_VALID_JSON =
      generateStringFromResource("jsonAssets/PostPlatformUserValid.json");
  public static final String POST_PLATFORM_USER_INVALID_JSON =
      generateStringFromResource("jsonAssets/PostPlatformUserInvalid.json");
  public static final String PATCH_PLATFORM_USER_VALID_JSON =
      generateStringFromResource("jsonAssets/PatchPlatformUserValid.json");
  public static final String PATCH_PLATFORM_USER_INVALID_JSON =
      generateStringFromResource("jsonAssets/PatchPlatformUserInvalid.json");

  public static final String POST_DRIVER_VALID_JSON = generateStringFromResource("jsonAssets/PostDriverValid.json");
  public static final String POST_DRIVER_INVALID_JSON = generateStringFromResource("jsonAssets/PostDriverInvalid.json");

  public static final String POST_JOURNEY_VALID_JSON = generateStringFromResource("jsonAssets/PostJourneyValid.json");
  public static final String POST_JOURNEY_INVALID_JSON =
      generateStringFromResource("jsonAssets/PostJourneyInvalid.json");
  public static final String PATCH_JOURNEY_VALID_JSON =
      generateStringFromResource("jsonAssets/PatchJourneyValid.json");
  public static final String PATCH_JOURNEY_INVALID_JSON =
      generateStringFromResource("jsonAssets/PatchJourneyInvalid.json");

  private Constants() {}

  private static String generateStringFromResource(final String path) {
    try {
      return Resources.toString(Resources.getResource(path), Charsets.UTF_8);
    } catch (IOException ex) {
      return "Cannot retrieve resource entity";
    }
  }
}
