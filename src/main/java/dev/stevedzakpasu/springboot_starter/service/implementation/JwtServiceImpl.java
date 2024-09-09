package dev.stevedzakpasu.springboot_starter.service.implementation;

import static dev.stevedzakpasu.springboot_starter.constant.TokenExpiryTime.*;

import dev.stevedzakpasu.springboot_starter.security.JwtAuthenticationToken;
import dev.stevedzakpasu.springboot_starter.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

  private static final String TOKEN_TYPE_CLAIM = "tokenType";

  @Value("${jwt.secret}")
  private String secretKey;

  private SecretKey key;

  @PostConstruct
  public void init() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    key = Keys.hmacShaKeyFor(keyBytes);
  }

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public String generateLoginToken(UserDetails userDetails) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put(TOKEN_TYPE_CLAIM, "LOGIN");
    return generateToken(extraClaims, userDetails, LOGIN_TOKEN_EXPIRY);
  }

  @Override
  public String generatePasswordResetToken(UserDetails userDetails) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put(TOKEN_TYPE_CLAIM, "PASSWORD_RESET");
    return generateToken(extraClaims, userDetails, PASSWORD_RESET_TOKEN_EXPIRY);
  }

  @Override
  public String generateEmailVerificationToken(UserDetails userDetails) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put(TOKEN_TYPE_CLAIM, "EMAIL_VERIFICATION");
    return generateToken(extraClaims, userDetails, EMAIL_VERIFICATION_TOKEN_EXPIRY);
  }

  @Override
  public String generateToken(
      Map<String, Object> extraClaims, UserDetails userDetails, long expirationTime) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(key)
        .compact();
  }

  @Override
  public boolean isLoginTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()))
        && isTokenExpired(token)
        && "LOGIN"
            .equals(extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class)));
  }

  @Override
  public boolean isVerificationTokenValid(String token) {
    return (isTokenExpired(token)
        && "EMAIL_VERIFICATION"
            .equals(extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class))));
  }

  @Override
  public boolean isPasswordResetTokenValid(String token) {
    return (isTokenExpired(token)
        && "PASSWORD_RESET"
            .equals(extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class))));
  }

  @Override
  public JwtAuthenticationToken generateJwtAuthenticationToken(
      UserDetails userDetails, long expirationTime) {
    String token = generateLoginToken(userDetails);
    return new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private boolean isTokenExpired(String token) {
    return !extractExpiration(token).before(new Date());
  }
}
