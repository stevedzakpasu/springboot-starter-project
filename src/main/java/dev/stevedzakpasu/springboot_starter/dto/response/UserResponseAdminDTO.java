package dev.stevedzakpasu.springboot_starter.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.stevedzakpasu.springboot_starter.entity.enums.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseAdminDTO(
    UUID uuid,
    String name,
    String email,
    String phone,
    String profileImage,
    Role role,
    boolean emailVerified,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updatedAt) {}
