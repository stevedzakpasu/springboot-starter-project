package dev.stevedzakpasu.springboot_starter.service.implementation;

import dev.stevedzakpasu.springboot_starter.entity.User;
import dev.stevedzakpasu.springboot_starter.event.UserCreatedEvent;
import dev.stevedzakpasu.springboot_starter.service.AsyncEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncEventServiceImpl implements AsyncEventService {
  private final ApplicationEventPublisher eventPublisher;

  @Async
  @Override
  public void publishUserCreatedEvent(User user) {
    eventPublisher.publishEvent(new UserCreatedEvent(this, user));
  }
}
