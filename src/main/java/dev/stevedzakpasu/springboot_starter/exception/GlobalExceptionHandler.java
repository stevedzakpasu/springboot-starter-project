package dev.stevedzakpasu.springboot_starter.exception;

import dev.stevedzakpasu.springboot_starter.dto.internal.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations()
        .forEach(
            violation -> {
              String propertyPath = violation.getPropertyPath().toString();
              String message = violation.getMessage();
              errors.put(propertyPath, message);
            });
    logger.error("ConstraintViolationException: {}", errors);
    ApiResponse<Map<String, String>> response = ApiResponse.error("Validation failed", errors);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    logger.error("IllegalArgumentException: {}", ex.getMessage());
    ApiResponse<String> response = ApiResponse.error(ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ApiResponse<String>> handleUserAlreadyExistsException(
      UserAlreadyExistsException ex) {
    logger.error("UserAlreadyExistsException: {}", ex.getMessage());
    ApiResponse<String> response = ApiResponse.error(ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(BlankFieldValidationException.class)
  public ResponseEntity<ApiResponse<String>> handleBlankFieldValidationException(
      BlankFieldValidationException ex) {
    logger.error("BlankFieldValidationException: {}", ex.getBlankFields());
    String errorMessage =
        "The following fields cannot be blank: " + String.join(", ", ex.getBlankFields());
    ApiResponse<String> response = ApiResponse.error(errorMessage, null);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EmailException.class)
  public ResponseEntity<ApiResponse<String>> handleEmailVerificationException(EmailException ex) {
    logger.error("EmailException: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error(ex.getMessage(), null));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(UserNotFoundException ex) {
    logger.error("UserNotFoundException: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiResponse.error(ex.getMessage(), null));
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<ApiResponse<String>> handleInvalidTokenException(InvalidTokenException ex) {
    logger.error("InvalidTokenException: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.error(ex.getMessage(), null));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiResponse<String>> handleAuthenticationException(
      AuthenticationException ex) {
    logger.error("AuthenticationException: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ApiResponse.error(ex.getMessage(), null));
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ApiResponse<String>> handleAuthorizationDeniedException(
      AuthorizationDeniedException ex) {
    logger.error("AuthorizationDeniedException: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ApiResponse.error(ex.getMessage(), null));
  }

  @ExceptionHandler(OAuth2AuthenticationException.class)
  public ResponseEntity<ApiResponse<String>> handleOAuth2AuthenticationException(
      OAuth2AuthenticationException ex) {
    logger.error("OAuth2AuthenticationException: {}", ex.getMessage());
    ApiResponse<String> response =
        ApiResponse.error("OAuth2 Authentication Error: " + ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(
      BadCredentialsException ex) {
    logger.error("BadCredentialsException: {}", ex.getMessage());
    ApiResponse<String> response = ApiResponse.error("Invalid JWT token: " + ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException ex) {
    logger.error("IllegalStateException: {}", ex.getMessage());
    ApiResponse<String> response = ApiResponse.error(ex.getMessage(), null);
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }
}
