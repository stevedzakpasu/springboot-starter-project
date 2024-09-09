package dev.stevedzakpasu.springboot_starter.service.implementation;

import dev.stevedzakpasu.springboot_starter.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String email;

  @Value("${app.url}")
  private String appUrl;

  @Override
  public void sendEmail(String to, String subject, String body) throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(body, true);
    helper.setFrom(email);
    mailSender.send(mimeMessage);
  }

  @Override
  public void sendVerificationEmail(String to, String token) throws MessagingException {
    String verificationUrl = appUrl + "/verify-email?token=" + token;
    String subject = "Email Verification";
    String body =
        "<p>Please click the link below to verify your account:</p>"
            + "<a href=\""
            + verificationUrl
            + "\">Verify Account</a>";
    sendEmail(to, subject, body);
  }

  @Override
  public void sendForgotPasswordEmail(String to, String token) throws MessagingException {
    String verificationUrl = appUrl + "/password-reset?token=" + token;
    String subject = "Email Verification";
    String body =
        "<p>Please click the link below to reset your password:</p>"
            + "<a href=\""
            + verificationUrl
            + "\">Reset Password</a>";
    sendEmail(to, subject, body);
  }
}
