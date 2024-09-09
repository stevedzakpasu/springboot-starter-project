package dev.stevedzakpasu.springboot_starter.service.implementation;

import static dev.stevedzakpasu.springboot_starter.constant.ErrorMessage.*;

import dev.stevedzakpasu.springboot_starter.dto.request.ChangePasswordDTO;
import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseAdminDTO;
import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseDTO;
import dev.stevedzakpasu.springboot_starter.entity.User;
import dev.stevedzakpasu.springboot_starter.exception.EmailException;
import dev.stevedzakpasu.springboot_starter.exception.InvalidTokenException;
import dev.stevedzakpasu.springboot_starter.exception.UserNotFoundException;
import dev.stevedzakpasu.springboot_starter.mapper.UserMapper;
import dev.stevedzakpasu.springboot_starter.repository.UserRepository;
import dev.stevedzakpasu.springboot_starter.service.EmailService;
import dev.stevedzakpasu.springboot_starter.service.JwtService;
import dev.stevedzakpasu.springboot_starter.service.UserService;
import dev.stevedzakpasu.springboot_starter.util.AuthenticationUtil;
import jakarta.mail.MessagingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final EmailService emailService;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void sendEmailVerificationLink(String email) throws EmailException {
    try {
      String token = generateVerificationToken(email);
      emailService.sendVerificationEmail(email, token);
    } catch (MessagingException e) {
      throw new EmailException(FAILED_TO_SEND_VERIFICATION_EMAIL_MESSAGE, e);
    }
  }

  private String generateVerificationToken(String email) {
    return jwtService.generateEmailVerificationToken(
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE)));
  }

  @Override
  public UserResponseDTO getCurrentUser() {
    User user = AuthenticationUtil.getAuthenticatedUser();
    return UserMapper.INSTANCE.toUserResponseDTO(user);
  }

  @Override
  public void sendPasswordResetLink(String email) {
    try {
      String token = generatePasswordResetToken(email);
      emailService.sendForgotPasswordEmail(email, token);
    } catch (MessagingException e) {
      throw new EmailException(FAILED_TO_SEND_FORGOT_PASSWORD_EMAIL_MESSAGE, e);
    }
  }

  private String generatePasswordResetToken(String email) {
    return jwtService.generatePasswordResetToken(
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE)));
  }

  @Override
  public void changePassword(ChangePasswordDTO changePasswordDTO) {
    User user = AuthenticationUtil.getAuthenticatedUser();
    if (!passwordEncoder.matches(changePasswordDTO.oldPassword(), user.getPassword())) {
      throw new AuthenticationException(INVALID_PASSWORD_MESSAGE) {};
    }
    user.setPassword(passwordEncoder.encode(changePasswordDTO.newPassword()));
    userRepository.save(user);
  }

  @Override
  public void resetPassword(String token, String newPassword) {
    if (!jwtService.isPasswordResetTokenValid(token)) {
      throw new InvalidTokenException(INVALID_TOKEN_MESSAGE) {};
    }
    User user =
        userRepository
            .findByEmail(jwtService.extractUsername(token))
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

  @Override
  public void verifyEmail(String token) {
    if (!jwtService.isVerificationTokenValid(token)) {
      throw new InvalidTokenException(INVALID_TOKEN_MESSAGE) {};
    }
    User user =
        userRepository
            .findByEmail(jwtService.extractUsername(token))
            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    user.setEmailVerified(true);
    userRepository.save(user);
  }

  @Override
  public boolean isVerificationTokenValid(String token) {
    return jwtService.isVerificationTokenValid(token);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserResponseAdminDTO> getAllUsers() {
    return UserMapper.INSTANCE.toUserResponseAdminDTOList(userRepository.findAll());
  }

  @Override
  public void changeEmail(String newEmail) {
    User user = AuthenticationUtil.getAuthenticatedUser();
    user.setEmail(newEmail);
    user.setEmailVerified(false);
    sendEmailVerificationLink(newEmail);
    userRepository.save(user);
  }
}
