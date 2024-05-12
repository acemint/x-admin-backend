package com.satusehat.endpoint.patient;

import com.satusehat.dto.request.patient.SatuSehatCreatePatientRequest;
import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.dto.response.patient.PatientCreationResourceResponse;
import com.satusehat.dto.response.patient.PatientResourceResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.property.SatuSehatPropertyHolder;
import lombok.Builder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class SatuSehatRegisterPatientByNIKEndpoint implements
    SatuSehatEndpoint<PatientCreationResourceResponse> {

  private static final String PATH = "/Patient";
  private static final String HTTP_METHOD = "POST";
  private static final Map.Entry<String, String> AUTHORIZATION_HEADER = Map.entry("Authorization", "Bearer ");

  private String authToken;
  private SatuSehatCreatePatientRequest satuSehatCreatePatientRequest;

  @Builder
  public SatuSehatRegisterPatientByNIKEndpoint(SatuSehatCreatePatientRequest satuSehatCreatePatientRequest) {
    this.satuSehatCreatePatientRequest = satuSehatCreatePatientRequest;
  }

  @Override
  public ResponseEntity<PatientCreationResourceResponse> getMethodCall() {
    RestClient restClient = RestClient.builder()
        .baseUrl(SatuSehatPropertyHolder.getInstance().getBaseUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    return restClient.method(HttpMethod.valueOf(HTTP_METHOD))
        .uri(uriBuilder -> uriBuilder
            .path(PATH)
            .build())
        .header(AUTHORIZATION_HEADER.getKey(), AUTHORIZATION_HEADER.getValue() + this.authToken)
        .body(satuSehatCreatePatientRequest)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {});
  }

  @Override
  public SatuSehatEndpoint<PatientCreationResourceResponse> setAuthToken(String authToken) {
    this.authToken = authToken;
    return this;
  }

}
