package dev.stevedzakpasu.springboot_starter.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessage {
  public static final String USER_NOT_FOUND_MESSAGE = "User not found";
  public static final String INVALID_CREDENTIALS_MESSAGE = "Invalid credentials";
  public static final String FAILED_TO_SEND_VERIFICATION_EMAIL_MESSAGE =
      "Failed to send verification email.";
  public static final String FAILED_TO_SEND_FORGOT_PASSWORD_EMAIL_MESSAGE =
      "Failed to send forgot password email.";
  public static final String INVALID_PASSWORD_MESSAGE = "Invalid password";
  public static final String INVALID_TOKEN_MESSAGE = "Invalid token";
  public static final String NOT_AUTHENTICATED_MESSAGE = "You are not authenticated.";
  public static final String AUTHENTICATED_PRINCIPAL_NOT_USER_MESSAGE =
      "Authenticated principal is not of type User.";
  public static final String USER_ALREADY_EXISTS_MESSAGE = "User with this email already exists";
}
