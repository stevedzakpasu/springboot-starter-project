package dev.stevedzakpasu.springboot_starter.security;

import static dev.stevedzakpasu.springboot_starter.constant.ErrorMessage.INVALID_TOKEN_MESSAGE;
import static dev.stevedzakpasu.springboot_starter.constant.ErrorMessage.USER_NOT_FOUND_MESSAGE;
import static dev.stevedzakpasu.springboot_starter.constant.TokenExpiryTime.LOGIN_TOKEN_EXPIRY;

import dev.stevedzakpasu.springboot_starter.entity.User;
import dev.stevedzakpasu.springboot_starter.exception.UserNotFoundException;
import dev.stevedzakpasu.springboot_starter.repository.UserRepository;
import dev.stevedzakpasu.springboot_starter.service.CustomOAuth2UserService;
import dev.stevedzakpasu.springboot_starter.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2AuthorizedClientService authorizedClientService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
      return handleJwtAuthentication(jwtAuthenticationToken);
    } else if (authentication instanceof OAuth2AuthenticationToken oauth2AuthenticationToken) {
      return handleOAuth2Authentication(oauth2AuthenticationToken);
    }
    return null;
  }

  private Authentication handleJwtAuthentication(JwtAuthenticationToken authentication) {
    String token = authentication.getToken();
    if (jwtService.isLoginTokenValid(token, authentication.getPrincipal())) {
      String userEmail = jwtService.extractUsername(token);
      User user =
          userRepository
              .findByEmail(userEmail)
              .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
      return jwtService.generateJwtAuthenticationToken(user, LOGIN_TOKEN_EXPIRY);
    }
    throw new BadCredentialsException(INVALID_TOKEN_MESSAGE);
  }

  private Authentication handleOAuth2Authentication(OAuth2AuthenticationToken authentication) {
    String clientRegistrationId = authentication.getAuthorizedClientRegistrationId();
    ClientRegistration clientRegistration =
        authorizedClientService
            .loadAuthorizedClient(clientRegistrationId, authentication.getName())
            .getClientRegistration();
    OAuth2AccessToken accessToken =
        authorizedClientService
            .loadAuthorizedClient(clientRegistrationId, authentication.getName())
            .getAccessToken();
    OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);
    OAuth2User oAuth2User = customOAuth2UserService.loadUser(userRequest);
    return new OAuth2AuthenticationToken(
        oAuth2User, oAuth2User.getAuthorities(), clientRegistration.getRegistrationId());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthenticationToken.class.isAssignableFrom(authentication)
        || OAuth2AuthenticationToken.class.isAssignableFrom(authentication);
  }
}
