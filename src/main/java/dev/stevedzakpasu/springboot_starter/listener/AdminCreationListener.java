package dev.stevedzakpasu.springboot_starter.listener;

import dev.stevedzakpasu.springboot_starter.config.AdminConfig;
import dev.stevedzakpasu.springboot_starter.entity.User;
import dev.stevedzakpasu.springboot_starter.entity.enums.Role;
import dev.stevedzakpasu.springboot_starter.event.AdminCreationEvent;
import dev.stevedzakpasu.springboot_starter.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminCreationListener implements ApplicationListener<AdminCreationEvent> {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AdminConfig adminConfig;

  @Override
  public void onApplicationEvent(@NonNull AdminCreationEvent event) {
    User superUser =
        User.builder()
            .email(adminConfig.adminEmail)
            .role(Role.ADMIN)
            .name(adminConfig.adminName)
            .password(passwordEncoder.encode(adminConfig.adminPassword))
            .phone(adminConfig.adminPhone)
            .authProvider("LOCAL")
            .emailVerified(true)
            .build();
    userRepository.save(superUser);
  }
}
