package dev.stevedzakpasu.springboot_starter.service;

import dev.stevedzakpasu.springboot_starter.entity.User;

public interface AsyncEventService {
  void publishUserCreatedEvent(User user);
}
