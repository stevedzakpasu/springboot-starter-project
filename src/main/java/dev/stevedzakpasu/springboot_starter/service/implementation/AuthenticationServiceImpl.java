package dev.stevedzakpasu.springboot_starter.service.implementation;

import static dev.stevedzakpasu.springboot_starter.constant.ErrorMessage.*;

import dev.stevedzakpasu.springboot_starter.dto.request.UserLoginDTO;
import dev.stevedzakpasu.springboot_starter.dto.request.UserRegisterDTO;
import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseDTO;
import dev.stevedzakpasu.springboot_starter.entity.User;
import dev.stevedzakpasu.springboot_starter.entity.enums.Role;
import dev.stevedzakpasu.springboot_starter.exception.InvalidCredentialsException;
import dev.stevedzakpasu.springboot_starter.exception.UserAlreadyExistsException;
import dev.stevedzakpasu.springboot_starter.exception.UserNotFoundException;
import dev.stevedzakpasu.springboot_starter.mapper.UserMapper;
import dev.stevedzakpasu.springboot_starter.repository.UserRepository;
import dev.stevedzakpasu.springboot_starter.security.CustomAuthenticationProvider;
import dev.stevedzakpasu.springboot_starter.service.AsyncEventService;
import dev.stevedzakpasu.springboot_starter.service.AuthenticationService;
import dev.stevedzakpasu.springboot_starter.service.JwtService;
import dev.stevedzakpasu.springboot_starter.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final CustomAuthenticationProvider authenticationProvider;
  private final AsyncEventService asyncEventService;

  @Override
  public UserResponseDTO register(UserRegisterDTO userRegisterDTO) {
    ValidationUtil.validateNotBlank(userRegisterDTO);
    if (userRepository.existsByEmail(userRegisterDTO.email())) {
      throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MESSAGE);
    }
    User user = createUserFromDTO(userRegisterDTO);
    User savedUser = userRepository.save(user);
    asyncEventService.publishUserCreatedEvent(user);
    return UserMapper.INSTANCE.toUserResponseDTO(savedUser);
  }

  @Override
  public String login(UserLoginDTO userLoginDTO) throws AuthenticationException {
    ValidationUtil.validateNotBlank(userLoginDTO);
    User user =
        userRepository
            .findByEmail(userLoginDTO.email())
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    try {
      Authentication authentication =
          new UsernamePasswordAuthenticationToken(user.getEmail(), userLoginDTO.password());
      authenticationProvider.authenticate(authentication);
    } catch (AuthenticationException e) {
      throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE, e);
    }
    return jwtService.generateLoginToken(user);
  }

  private User createUserFromDTO(UserRegisterDTO userRegisterDTO) {
    String encodedPassword = passwordEncoder.encode(userRegisterDTO.password());
    return User.builder()
        .name(userRegisterDTO.name())
        .email(userRegisterDTO.email())
        .password(encodedPassword)
        .phone(userRegisterDTO.phone())
        .role(Role.USER)
        .authProvider("LOCAL")
        .build();
  }
}
