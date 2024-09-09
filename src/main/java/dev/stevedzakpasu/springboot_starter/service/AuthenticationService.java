package dev.stevedzakpasu.springboot_starter.service;

import dev.stevedzakpasu.springboot_starter.dto.request.UserLoginDTO;
import dev.stevedzakpasu.springboot_starter.dto.request.UserRegisterDTO;
import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseDTO;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationService {
  String login(UserLoginDTO userLoginDTO) throws AuthenticationException;

  UserResponseDTO register(UserRegisterDTO userRegisterDTO);
}
