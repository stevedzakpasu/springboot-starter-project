package dev.stevedzakpasu.springboot_starter.util;

import dev.stevedzakpasu.springboot_starter.exception.BlankFieldValidationException;
import io.micrometer.common.util.StringUtils;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {
  public static <T> void validateNotBlank(T dto) {
    if (dto == null) {
      throw new IllegalArgumentException("DTO object cannot be null");
    }
    List<String> blankFields =
        Arrays.stream(dto.getClass().getDeclaredFields())
            .filter(
                field -> {
                  try {
                    Object value = field.get(dto);
                    return value instanceof String string && StringUtils.isBlank(string);
                  } catch (IllegalAccessException e) {
                    return false;
                  }
                })
            .map(Field::getName)
            .toList();
    if (!blankFields.isEmpty()) {
      throw new BlankFieldValidationException(blankFields);
    }
  }
}
