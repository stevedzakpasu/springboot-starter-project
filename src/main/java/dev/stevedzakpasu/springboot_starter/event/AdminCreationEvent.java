package dev.stevedzakpasu.springboot_starter.event;

import org.springframework.context.ApplicationEvent;

public class AdminCreationEvent extends ApplicationEvent {
  public AdminCreationEvent(Object source) {
    super(source);
  }
}
