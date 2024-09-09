package dev.stevedzakpasu.springboot_starter.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class BlankFieldValidationException extends RuntimeException {
  private final List<String> blankFields;

  public BlankFieldValidationException(List<String> blankFields) {
    super("The following fields cannot be blank: " + String.join(", ", blankFields));
    this.blankFields = blankFields;
  }
}
