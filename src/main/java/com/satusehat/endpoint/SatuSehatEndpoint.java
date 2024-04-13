package com.satusehat.endpoint;

import org.springframework.http.ResponseEntity;

public interface SatuSehatEndpoint<T> {

  ResponseEntity<T> getMethodCall();
  SatuSehatEndpoint<T> setAuthToken(String authToken);

}
