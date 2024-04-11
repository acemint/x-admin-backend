package com.clinic.xadmin.security.util;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

@Deprecated
public interface JwtTokenUtil {
  String generateJwtToken(Authentication authentication);
  Claims getClaimsFromToken(String token);
  long getExpiryTime();

}
