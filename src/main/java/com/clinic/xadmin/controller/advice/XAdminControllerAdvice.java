package com.clinic.xadmin.controller.advice;

import com.clinic.xadmin.dto.response.StandardizedErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class XAdminControllerAdvice {

  @ExceptionHandler(value = { Exception.class })
  public ResponseEntity<StandardizedErrorResponse> handleUnexpectedException(Exception exception) {
    log.error("unchecked exception", exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(StandardizedErrorResponse.builder()
            .message(exception.getMessage())
            .build());
  }

  @ExceptionHandler(value = {
      MethodArgumentNotValidException.class
  })
  public ResponseEntity<StandardizedErrorResponse> handleMethodArgNotValid(MethodArgumentNotValidException exception) {
    String errorMessage = "Method argument is invalid";

    log.error(errorMessage, exception);

    Map<String, String> fieldValidations = new HashMap<>();
    for (ObjectError e : exception.getBindingResult().getAllErrors()) {
      fieldValidations.put(((FieldError) e).getField(), e.getDefaultMessage());
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(StandardizedErrorResponse.builder()
            .message(errorMessage)
            .fields(fieldValidations)
            .build());
  }

  @ExceptionHandler(value = { BadCredentialsException.class,
      AccessDeniedException.class,
      UsernameNotFoundException.class
  })
  public ResponseEntity<StandardizedErrorResponse> forbiddenAuthentication(Exception exception) {
    String error = "forbidden access";

    log.error(error, exception);
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(StandardizedErrorResponse.builder()
            .message(exception.getMessage())
            .build());
  }

}
