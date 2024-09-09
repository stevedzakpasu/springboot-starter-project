package dev.stevedzakpasu.springboot_starter.dto.internal;

import java.time.LocalDateTime;

public record ApiResponse<T>(String status, String message, T data, LocalDateTime timestamp) {
  public ApiResponse(String status, String message, T data) {
    this(status, message, data, LocalDateTime.now());
  }

  public static <T> ApiResponse<T> success(String message, T data) {
    return new ApiResponse<>("success", message, data);
  }

  public static <T> ApiResponse<T> error(String message, T data) {
    return new ApiResponse<>("error", message, data);
  }
}
