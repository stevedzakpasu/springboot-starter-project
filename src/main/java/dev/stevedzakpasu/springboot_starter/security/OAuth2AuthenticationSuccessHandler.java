package dev.stevedzakpasu.springboot_starter.security;

import static dev.stevedzakpasu.springboot_starter.constant.TokenExpiryTime.LOGIN_TOKEN_EXPIRY;

import dev.stevedzakpasu.springboot_starter.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtService jwtService;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    Map<String, Object> claims = new HashMap<>();
    claims.put("tokenType", "LOGIN");

    String token = jwtService.generateToken(claims, oAuth2User, LOGIN_TOKEN_EXPIRY);

    String redirectUrl = request.getParameter("redirect_uri");
    if (redirectUrl != null && !redirectUrl.isEmpty()) {
      getRedirectStrategy().sendRedirect(request, response, redirectUrl + "?token=" + token);
    } else {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write("{\"token\": \"" + token + "\"}");
    }
  }
}
