package com.clinic.xadmin.security.filter;

import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.authprovider.CustomUserDetailsFactory;
import com.clinic.xadmin.security.util.JwtTokenUtil;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component(value = JWTTokenRequestFilter.JWT_TOKEN_FILTER_BEAN)
public class JWTTokenRequestFilter extends OncePerRequestFilter {

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
    final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (Objects.isNull(token)) {
      chain.doFilter(request, response);
      return;
    }

    // Get jwt token and validate
    if (!jwtTokenUtil.isStillValid(token)) {
      chain.doFilter(request, response);
      return;
    }

    // Get user identity and set it on the spring security context
    Employee employee = employeeRepository.findEmployeeByEmailAddress(jwtTokenUtil.getClaimsFromToken(token).getSubject());
    if (Objects.isNull(employee)) {
      chain.doFilter(request, response);
      return;
    }

    // if all passed then create authentication object and insert it into SecurityContext
    CustomUserDetails customUserDetails = CustomUserDetailsFactory.createFrom(employee);
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities()
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }
}
