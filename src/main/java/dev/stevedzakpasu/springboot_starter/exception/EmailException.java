package dev.stevedzakpasu.springboot_starter.exception;

public class EmailException extends RuntimeException {
  public EmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
