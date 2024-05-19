package com.satusehat.endpoint.restclients;

import com.satusehat.property.SatuSehatPropertyHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class SatuSehatRestClient {

  private static final Map.Entry<String, String> AUTHORIZATION_HEADER = Map.entry("Authorization", "Bearer ");

  public static RestClient getRestClient(String token) {
    return RestClient.builder()
        .baseUrl(SatuSehatPropertyHolder.getInstance().getBaseUrl())
        .defaultHeaders(httpHeaders -> {
          httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
          httpHeaders.add(AUTHORIZATION_HEADER.getKey(), AUTHORIZATION_HEADER.getValue() + token);
        })
        .build();
  }

  public static RestClient getAuthenticationRestClient() {
    return RestClient.builder()
        .baseUrl(SatuSehatPropertyHolder.getInstance().getAuthUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

}
