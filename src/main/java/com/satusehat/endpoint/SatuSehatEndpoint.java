package com.satusehat.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

public interface SatuSehatEndpoint<T> {

  ResponseEntity<T> getMethodCall() throws HttpClientErrorException, HttpServerErrorException;
  SatuSehatEndpoint<T> setAuthToken(String authToken);

}
