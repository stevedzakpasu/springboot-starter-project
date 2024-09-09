package dev.stevedzakpasu.springboot_starter.service;

import jakarta.mail.MessagingException;

public interface EmailService {
  void sendEmail(String to, String subject, String body) throws MessagingException;

  void sendVerificationEmail(String to, String token) throws MessagingException;

  void sendForgotPasswordEmail(String to, String token) throws MessagingException;
}
