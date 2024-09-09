package dev.stevedzakpasu.springboot_starter.util;

import static dev.stevedzakpasu.springboot_starter.constant.ErrorMessage.AUTHENTICATED_PRINCIPAL_NOT_USER_MESSAGE;
import static dev.stevedzakpasu.springboot_starter.constant.ErrorMessage.NOT_AUTHENTICATED_MESSAGE;

import dev.stevedzakpasu.springboot_starter.entity.User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class AuthenticationUtil {
  public static User getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException(NOT_AUTHENTICATED_MESSAGE);
    }
    Object principal = authentication.getPrincipal();
    if (!(principal instanceof User)) {
      throw new IllegalStateException(AUTHENTICATED_PRINCIPAL_NOT_USER_MESSAGE);
    }

    return (User) principal;
  }
}
