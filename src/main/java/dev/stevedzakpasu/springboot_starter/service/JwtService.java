package dev.stevedzakpasu.springboot_starter.service;

import dev.stevedzakpasu.springboot_starter.security.JwtAuthenticationToken;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
  String extractUsername(String token);

  String generateToken(
      Map<String, Object> extraClaims, UserDetails userDetails, long expirationTime);

  String generateEmailVerificationToken(UserDetails userDetails);

  String generateLoginToken(UserDetails userDetails);

  String generatePasswordResetToken(UserDetails userDetails);

  JwtAuthenticationToken generateJwtAuthenticationToken(
      UserDetails userDetails, long expirationTime);

  boolean isLoginTokenValid(String token, UserDetails userDetails);

  boolean isVerificationTokenValid(String token);

  boolean isPasswordResetTokenValid(String token);
}
