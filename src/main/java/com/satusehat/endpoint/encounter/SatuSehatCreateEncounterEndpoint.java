package com.satusehat.endpoint.encounter;

import com.satusehat.dto.request.encounter.SatuSehatCreateEncounterRequest;
import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.dto.response.encounter.SatuSehatCreateEncounterResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.endpoint.restclients.SatuSehatRestClient;
import lombok.Builder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class SatuSehatCreateEncounterEndpoint implements
    SatuSehatEndpoint<SatuSehatCreateEncounterResponse> {

  private String authToken;
  private SatuSehatCreateEncounterRequest requestBody;

  @Builder
  public SatuSehatCreateEncounterEndpoint(SatuSehatCreateEncounterRequest requestBody) {
    this.requestBody = requestBody;
  }

  @Override
  public ResponseEntity<SatuSehatCreateEncounterResponse> performHttpRequest() {
    RestClient restClient = SatuSehatRestClient.getRestClient(this.authToken);

    return restClient.method(HttpMethod.POST)
        .uri(uriBuilder -> uriBuilder
            .path(SatuSehatEncounterPath.BASE_PATH)
            .build())
        .body(requestBody)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {});
  }

  @Override
  public SatuSehatEndpoint<SatuSehatCreateEncounterResponse> setAuthToken(String authToken) {
    this.authToken = authToken;
    return this;
  }

}
