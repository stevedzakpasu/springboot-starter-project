package dev.stevedzakpasu.springboot_starter.security;

import java.util.Collection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@EqualsAndHashCode(callSuper = true)
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private final UserDetails principal;
  @Getter private String token;

  public JwtAuthenticationToken(
      UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.token = token;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return token;
  }

  @Override
  public UserDetails getPrincipal() {
    return principal;
  }
}
