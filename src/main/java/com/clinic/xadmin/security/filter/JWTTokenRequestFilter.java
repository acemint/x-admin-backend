package com.clinic.xadmin.security.filter;

import com.clinic.xadmin.security.context.ThreadLocalAuthenticationHolder;
import com.clinic.xadmin.security.constant.CookieName;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.authprovider.CustomUserDetailsFactory;
import com.clinic.xadmin.security.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Deprecated
@Slf4j
//@Component(value = JWTTokenRequestFilter.JWT_TOKEN_FILTER_BEAN)
public class JWTTokenRequestFilter extends OncePerRequestFilter {

  // component is commented so that it won't be picked-up by spring-security

  public static final String JWT_TOKEN_FILTER_BEAN = "JWT_TOKEN_FILTER_BEAN";

  private final EmployeeRepository employeeRepository;
  private final JwtTokenUtil jwtTokenUtil;

  @Autowired
  public JWTTokenRequestFilter(JwtTokenUtil jwtTokenUtil,
      EmployeeRepository employeeRepository) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.employeeRepository = employeeRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    // Get authorization header and validate
    String token = this.extractJwtFromCookie(request);
    if (Objects.isNull(token)) {
      chain.doFilter(request, response);
      return;
    }

    // Get user identity and set it on the spring security context
    Claims claims = jwtTokenUtil.getClaimsFromToken(token);
    if (Objects.isNull(claims)) {
      chain.doFilter(request, response);
      return;
    }

    // Create authentication object and insert it into SecurityContext
    Employee employee = employeeRepository.findEmployeeByEmailAddress(claims.getSubject());
    CustomUserDetails customUserDetails = CustomUserDetailsFactory.createFrom(employee);
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities()
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    log.debug("AUTHENTICATION SET");
    ThreadLocalAuthenticationHolder.authentication.set(authentication);
    chain.doFilter(request, response);
    log.debug("AUTHENTICATION DELETED");
    ThreadLocalAuthenticationHolder.authentication.remove();
  }

  private String extractJwtFromCookie(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return null;
    }
    for (Cookie cookie : request.getCookies()) {
      if (CookieName.CREDENTIAL.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
