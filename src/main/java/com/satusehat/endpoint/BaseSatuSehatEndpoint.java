package com.satusehat.endpoint;

import org.springframework.http.ResponseEntity;

public interface BaseSatuSehatEndpoint<T> {

  ResponseEntity<T> getMethodCall();
  BaseSatuSehatEndpoint<T> setAuthToken(String authToken);

}
