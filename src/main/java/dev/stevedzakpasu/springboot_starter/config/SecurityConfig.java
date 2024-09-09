package dev.stevedzakpasu.springboot_starter.config;

import dev.stevedzakpasu.springboot_starter.security.CustomAuthenticationProvider;
import dev.stevedzakpasu.springboot_starter.security.JWTFilter;
import dev.stevedzakpasu.springboot_starter.security.OAuth2AuthenticationSuccessHandler;
import dev.stevedzakpasu.springboot_starter.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JWTFilter jwtFilter;
  private final CustomAuthenticationProvider customAuthenticationProvider;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(
            cors ->
                cors.configurationSource(
                    request -> {
                      CorsConfiguration configuration = new CorsConfiguration();
                      configuration.setAllowedOrigins(List.of("https://trusted-origin.com"));
                      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                      configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                      configuration.setAllowCredentials(true);
                      return configuration;
                    }))
        .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorizeRequests -> {
              authorizeRequests.requestMatchers("/api/v1/auth/**").permitAll();
              authorizeRequests.requestMatchers("/api/admin/**").hasRole("ADMIN");
              authorizeRequests.anyRequest().authenticated();
            })
        .authenticationProvider(customAuthenticationProvider)
        .oauth2Login(
            oauth2 ->
                oauth2
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                    .successHandler(oAuth2AuthenticationSuccessHandler))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(
                        (request, response, authException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                    .accessDeniedHandler(
                        (request, response, accessDeniedException) ->
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden")));
    return http.build();
  }
}
