package dev.stevedzakpasu.springboot_starter.dto.response;

import dev.stevedzakpasu.springboot_starter.entity.enums.Role;
import java.util.UUID;

public record UserResponseDTO(
    UUID uuid,
    String name,
    String email,
    String phone,
    String profileImage,
    Role role,
    boolean emailVerified,
    String authProvider) {}
