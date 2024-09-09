package dev.stevedzakpasu.springboot_starter.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenExpiryTime {

  public static final long LOGIN_TOKEN_EXPIRY = 1000L * 60 * 60 * 10; // 10 hours
  public static final long EMAIL_VERIFICATION_TOKEN_EXPIRY = 1000L * 60 * 60 * 24; // 24 hours
  public static final long PASSWORD_RESET_TOKEN_EXPIRY = 1000L * 60 * 60; // 1 hour
}
