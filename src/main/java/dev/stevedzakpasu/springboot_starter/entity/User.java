package dev.stevedzakpasu.springboot_starter.entity;

import dev.stevedzakpasu.springboot_starter.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

  @NotBlank(message = "Name cannot be blank")
  @Column(nullable = false)
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  @Column(nullable = false)
  private String email;

  @Size(min = 8, message = "Password should be at least 8 characters")
  private String password;

  @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Phone number should be valid")
  private String phone;

  private String profileImage;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  private boolean emailVerified;

  private String authProvider;

  private String authProviderId;

  @Transient
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getUsername() {
    return getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
