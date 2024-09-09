package dev.stevedzakpasu.springboot_starter.exception;

public class InvalidCredentialsException extends RuntimeException {

  public InvalidCredentialsException(String message, Throwable cause) {
    super(message, cause);
  }
}
