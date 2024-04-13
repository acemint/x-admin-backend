package com.satusehat.endpoint.patient;

import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.dto.response.patient.PatientResourceResponse;
import com.satusehat.endpoint.BaseSatuSehatEndpoint;
import com.satusehat.property.SatuSehatProperty;
import com.satusehat.property.SatuSehatPropertyHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class SatuSehatSearchPatientByNIKEndpoint implements BaseSatuSehatEndpoint<StandardizedResourceResponse<PatientResourceResponse>> {

  private static final String PATH = "/Patient";
  private static final String HTTP_METHOD = "GET";
  private static final Map.Entry<String, String> AUTHORIZATION_HEADER = Map.entry("Authorization", "Bearer ");

  private final String authToken;
  private final String nik;

  public SatuSehatSearchPatientByNIKEndpoint(String authToken, String nik) {
    this.authToken = authToken;
    this.nik = SatuSehatPropertyHolder.getInstance().getNikUrl() + nik;
  }

  @Override
  public ResponseEntity<StandardizedResourceResponse<PatientResourceResponse>> getMethodCall() {
    RestClient restClient = RestClient.builder()
        .baseUrl(SatuSehatPropertyHolder.getInstance().getBaseUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    return restClient.method(HttpMethod.valueOf(HTTP_METHOD))
        .uri(uriBuilder -> uriBuilder
            .path(PATH)
            .queryParam("identifier", this.nik)
            .build())
        .header(AUTHORIZATION_HEADER.getKey(), AUTHORIZATION_HEADER.getValue() + this.authToken)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {});
  }

}
