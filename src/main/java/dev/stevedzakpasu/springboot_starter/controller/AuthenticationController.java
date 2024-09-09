package dev.stevedzakpasu.springboot_starter.controller;

import static dev.stevedzakpasu.springboot_starter.constant.SuccessMessage.LOGIN_SUCCESSFUL_MESSAGE;
import static dev.stevedzakpasu.springboot_starter.constant.SuccessMessage.USER_REGISTERED_SUCCESSFULLY_MESSAGE;

import dev.stevedzakpasu.springboot_starter.dto.internal.ApiResponse;
import dev.stevedzakpasu.springboot_starter.dto.request.UserLoginDTO;
import dev.stevedzakpasu.springboot_starter.dto.request.UserRegisterDTO;
import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseDTO;
import dev.stevedzakpasu.springboot_starter.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<UserResponseDTO>> register(
      @RequestBody UserRegisterDTO userRegisterDTO) {
    UserResponseDTO userResponse = authenticationService.register(userRegisterDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(USER_REGISTERED_SUCCESSFULLY_MESSAGE, userResponse));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<String>> login(@RequestBody UserLoginDTO userLoginDTO) {
    String token = authenticationService.login(userLoginDTO);
    return ResponseEntity.ok(ApiResponse.success(LOGIN_SUCCESSFUL_MESSAGE, token));
  }
}
