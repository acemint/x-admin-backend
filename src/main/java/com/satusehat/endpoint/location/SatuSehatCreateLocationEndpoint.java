package com.satusehat.endpoint.location;

import com.satusehat.dto.request.location.SatuSehatCreateLocationRequest;
import com.satusehat.dto.response.location.SatuSehatCreateLocationResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.endpoint.restclients.SatuSehatRestClient;
import lombok.Builder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class SatuSehatCreateLocationEndpoint implements SatuSehatEndpoint<SatuSehatCreateLocationResponse> {

  private String authToken;
  private SatuSehatCreateLocationRequest requestBody;

  @Builder
  public SatuSehatCreateLocationEndpoint(SatuSehatCreateLocationRequest requestBody) {
    this.requestBody = requestBody;
  }

  @Override
  public ResponseEntity<SatuSehatCreateLocationResponse> performHttpRequest() {
    RestClient restClient = SatuSehatRestClient.getRestClient(this.authToken);

    return restClient.method(HttpMethod.PUT)
        .uri(uriBuilder -> uriBuilder
            .path(SatuSehatLocationPath.BASE_PATH)
            .build())
        .body(this.requestBody)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {});
  }

  @Override
  public SatuSehatEndpoint<SatuSehatCreateLocationResponse> setAuthToken(String authToken) {
    this.authToken = authToken;
    return this;
  }
}


