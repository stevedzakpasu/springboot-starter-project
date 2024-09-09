package dev.stevedzakpasu.springboot_starter.security;

import static dev.stevedzakpasu.springboot_starter.constant.ErrorMessage.USER_NOT_FOUND_MESSAGE;

import dev.stevedzakpasu.springboot_starter.exception.UserNotFoundException;
import dev.stevedzakpasu.springboot_starter.repository.UserRepository;
import dev.stevedzakpasu.springboot_starter.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final CustomAuthenticationProvider customAuthenticationProvider;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String jwt = authorizationHeader.substring(7);
    String username = jwtService.extractUsername(jwt);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails =
          userRepository
              .findByEmail(username)
              .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

      JwtAuthenticationToken authRequest =
          new JwtAuthenticationToken(userDetails, jwt, userDetails.getAuthorities());

      Authentication authResult = customAuthenticationProvider.authenticate(authRequest);

      if (authResult != null
          && jwtService.isLoginTokenValid(jwt, (UserDetails) authResult.getPrincipal())) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
                authResult.getPrincipal(), null, authResult.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      } else {
        throw new AuthenticationException("Invalid JWT token") {};
      }
    }
    filterChain.doFilter(request, response);
  }
}
