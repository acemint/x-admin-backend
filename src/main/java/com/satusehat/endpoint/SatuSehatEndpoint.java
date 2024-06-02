package com.satusehat.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

public interface SatuSehatEndpoint<T> {

  ResponseEntity<T> performHttpRequest() throws HttpClientErrorException, HttpServerErrorException;

  SatuSehatEndpoint<T> setAuthToken(String authToken);

}
