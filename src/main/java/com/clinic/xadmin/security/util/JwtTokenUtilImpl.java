package com.clinic.xadmin.security.util;

import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component(value = JwtTokenUtilImpl.BEAN_NAME)
public class JwtTokenUtilImpl implements JwtTokenUtil {

  public static final String BEAN_NAME = "JwtTokenUtil";

  @Value("${xadmin.jwt.secret-key}")
  private String jwtSecret;

  @Value("${xadmin.jwt.expiration-time-in-ms}")
  private int jwtExpirationMs;

  @Override
  public String generateJwtToken(Authentication authentication) {
    CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
    return Jwts.builder()
        .setSubject(userPrincipal.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName()))
        .compact();
  }

  @Override
  public Claims getClaimsFromToken(String token) {
    JwtParser parser = this.getDefaultJwtParser();
    return (Claims) parser.parse(token).getBody();
  }

  @Override
  public boolean isStillValid(String token) {
    try {
      Claims claims = (Claims) this.getDefaultJwtParser().parse(token).getBody();
      return true;
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  private JwtParser getDefaultJwtParser() {
    return Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
        .build();
  }

}
