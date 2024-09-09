package dev.stevedzakpasu.springboot_starter.service.implementation;

import dev.stevedzakpasu.springboot_starter.entity.User;
import dev.stevedzakpasu.springboot_starter.entity.enums.Role;
import dev.stevedzakpasu.springboot_starter.repository.UserRepository;
import dev.stevedzakpasu.springboot_starter.security.CustomOAuth2User;
import dev.stevedzakpasu.springboot_starter.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService
    implements CustomOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oauth2User = super.loadUser(userRequest);

    String email = oauth2User.getAttribute("email");
    String name = oauth2User.getAttribute("name");
    String googleId = oauth2User.getAttribute("sub");
    String picture = oauth2User.getAttribute("picture");

    if (email == null || name == null || googleId == null) {
      throw new OAuth2AuthenticationException("Missing required attributes from Google user info");
    }
    User user =
        userRepository
            .findByEmail(email)
            .orElseGet(
                () ->
                    User.builder()
                        .email(email)
                        .name(name)
                        .role(Role.USER)
                        .profileImage(picture)
                        .authProvider("GOOGLE")
                        .emailVerified(true)
                        .authProviderId(googleId)
                        .build());

    userRepository.save(user);
    return new CustomOAuth2User(oauth2User, user);
  }
}
