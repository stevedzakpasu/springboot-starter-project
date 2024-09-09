package dev.stevedzakpasu.springboot_starter.controller;

import static dev.stevedzakpasu.springboot_starter.constant.SuccessMessage.*;

import dev.stevedzakpasu.springboot_starter.dto.internal.ApiResponse;
import dev.stevedzakpasu.springboot_starter.dto.request.ChangePasswordDTO;
import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseAdminDTO;
import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseDTO;
import dev.stevedzakpasu.springboot_starter.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @GetMapping("/current-user")
  public ResponseEntity<ApiResponse<UserResponseDTO>> getCurrentUser() {
    return ResponseEntity.ok(
        ApiResponse.success(CURRENT_USER_DETAILS_RETRIEVED_MESSAGE, userService.getCurrentUser()));
  }

  @PostMapping("/change-password")
  public ResponseEntity<ApiResponse<Void>> changePassword(
      @RequestBody ChangePasswordDTO changePasswordDTO) {
    userService.changePassword(changePasswordDTO);
    return ResponseEntity.ok(ApiResponse.success(PASSWORD_CHANGED_SUCCESSFULLY_MESSAGE, null));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<ApiResponse<String>> sendForgotPasswordEmail(@RequestParam String email) {
    userService.sendPasswordResetLink(email);
    return ResponseEntity.ok(ApiResponse.success(FORGOT_PASSWORD_EMAIL_SENT_MESSAGE, null));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<ApiResponse<String>> resetPassword(
      @RequestParam String token, @RequestParam String newPassword) {
    userService.resetPassword(token, newPassword);
    return ResponseEntity.ok(ApiResponse.success(PASSWORD_RESET_SUCCESSFULLY_MESSAGE, null));
  }

  @PostMapping("/send-verification-email")
  public ResponseEntity<ApiResponse<String>> sendVerificationEmail(@RequestParam String email) {
    userService.sendEmailVerificationLink(email);
    return ResponseEntity.ok(ApiResponse.success(VERIFICATION_EMAIL_SENT_MESSAGE, null));
  }

  @PostMapping("/verify-email")
  public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
    userService.verifyEmail(token);
    return ResponseEntity.ok(ApiResponse.success(EMAIL_VERIFIED_SUCCESSFULLY_MESSAGE, null));
  }

  @PostMapping("/change-email")
  public ResponseEntity<ApiResponse<String>> changeEmail(@RequestParam String newEmail) {
    userService.changeEmail(newEmail);
    return ResponseEntity.ok(ApiResponse.success(EMAIL_CHANGED_SUCCESSFULLY_MESSAGE, null));
  }

  @GetMapping("/verify-token-validity")
  public ResponseEntity<ApiResponse<Boolean>> isVerificationTokenValid(@RequestParam String token) {
    boolean isValid = userService.isVerificationTokenValid(token);
    return ResponseEntity.ok(
        ApiResponse.success(VERIFICATION_TOKEN_VALIDATION_RESULT_MESSAGE, isValid));
  }

  @GetMapping("/all")
  public ResponseEntity<ApiResponse<List<UserResponseAdminDTO>>> getAllUsers() {
    return ResponseEntity.ok(
        ApiResponse.success(ALL_USERS_RETRIEVED_MESSAGE, userService.getAllUsers()));
  }
}
