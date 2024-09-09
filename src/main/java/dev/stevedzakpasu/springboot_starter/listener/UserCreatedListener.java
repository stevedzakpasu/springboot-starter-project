package dev.stevedzakpasu.springboot_starter.listener;

import dev.stevedzakpasu.springboot_starter.event.UserCreatedEvent;
import dev.stevedzakpasu.springboot_starter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreatedListener implements ApplicationListener<UserCreatedEvent> {

  private final UserService userService;

  @Override
  public void onApplicationEvent(UserCreatedEvent accountCreatedEvent) {
    userService.sendEmailVerificationLink(accountCreatedEvent.getUser().getEmail());
  }
}
