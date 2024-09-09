package dev.stevedzakpasu.springboot_starter.listener;

import dev.stevedzakpasu.springboot_starter.event.AdminCreationEvent;
import dev.stevedzakpasu.springboot_starter.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

  private final UserRepository userRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Value("${ADMIN_EMAIL}")
  private String adminEmail;

  @Override
  public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
    if (userRepository.findByEmail(adminEmail).isEmpty()) {
      eventPublisher.publishEvent(new AdminCreationEvent(this));
    }
  }
}
