package com.satusehat.endpoint.practitioner;

import com.satusehat.constant.KemkesURL;
import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.dto.response.practitioner.PractitionerSearchResourceResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.endpoint.restclients.SatuSehatRestClient;
import lombok.Builder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class SatuSehatSearchPractitionerEndpoint implements SatuSehatEndpoint<StandardizedResourceResponse<PractitionerSearchResourceResponse>> {

  private String authToken;
  private String nik;

  @Builder
  public SatuSehatSearchPractitionerEndpoint(String nik) {
    this.nik = nik;
  }

  @Override
  public ResponseEntity<StandardizedResourceResponse<PractitionerSearchResourceResponse>> performHttpRequest() {
    RestClient restClient = SatuSehatRestClient.getRestClient(this.authToken);

    return restClient.method(HttpMethod.valueOf(HttpMethod.GET.name()))
        .uri(uriBuilder -> uriBuilder
            .path(SatuSehatPractitionerPath.BASE_PATH)
            .queryParam("identifier", KemkesURL.Identity.NIK + "|" + this.nik)
            .build())
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {});
  }

  @Override
  public SatuSehatEndpoint<StandardizedResourceResponse<PractitionerSearchResourceResponse>> setAuthToken(
      String authToken) {
    this.authToken = authToken;
    return this;
  }

}
