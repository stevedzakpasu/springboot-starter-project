package dev.stevedzakpasu.springboot_starter.service;

import dev.stevedzakpasu.springboot_starter.dto.request.ChangePasswordDTO;
import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseAdminDTO;
import dev.stevedzakpasu.springboot_starter.dto.response.UserResponseDTO;
import dev.stevedzakpasu.springboot_starter.exception.EmailException;
import java.util.List;

public interface UserService {
  UserResponseDTO getCurrentUser();

  void changePassword(ChangePasswordDTO changePasswordDTO);

  void changeEmail(String newEmail);

  void sendEmailVerificationLink(String email) throws EmailException;

  void verifyEmail(String token);

  void sendPasswordResetLink(String email);

  void resetPassword(String token, String newPassword);

  boolean isVerificationTokenValid(String token);

  List<UserResponseAdminDTO> getAllUsers();
}
