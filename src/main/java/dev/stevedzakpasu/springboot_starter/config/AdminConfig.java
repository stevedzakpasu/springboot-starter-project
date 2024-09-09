package dev.stevedzakpasu.springboot_starter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AdminConfig {
  @Value("${ADMIN_EMAIL}")
  public String adminEmail;

  @Value("${ADMIN_NAME}")
  public String adminName;

  @Value("${ADMIN_PASSWORD}")
  public String adminPassword;

  @Value("${ADMIN_PHONE}")
  public String adminPhone;
}
