package com.clinic.xadmin.controller.advice;

import com.clinic.xadmin.controller.dto.StandardizedErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class XAdminControllerAdvice {

  @ExceptionHandler(value = { Exception.class })
  public ResponseEntity<StandardizedErrorResponse> handleUnexpectedException(Exception exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(StandardizedErrorResponse.builder()
            .message(exception.getMessage())
            .stackTrace(Arrays.stream(exception.getStackTrace()).findFirst().map(StackTraceElement::toString).orElse(null))
            .build());
  }

  @ExceptionHandler(value = { BadCredentialsException.class,
      AccessDeniedException.class,
      UsernameNotFoundException.class,
      MalformedJwtException.class,
      ExpiredJwtException.class,
      UnsupportedJwtException.class,
      SignatureException.class,
      SecurityException.class,
      ExpiredJwtException.class })
  public ResponseEntity<StandardizedErrorResponse> handleInvalidJwt(Exception exception) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(StandardizedErrorResponse.builder()
            .message(exception.getMessage())
            .stackTrace(Arrays.stream(exception.getStackTrace()).findFirst().map(StackTraceElement::toString).orElse(null))
            .build());
  }

}
