package com.satusehat.endpoint.location;

import com.satusehat.constant.coding.LocationPhysicalTypeEnum;
import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.dto.response.location.SatuSehatSearchLocationResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.endpoint.practitioner.SatuSehatPractitionerPath;
import com.satusehat.endpoint.restclients.SatuSehatRestClient;
import lombok.Builder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import java.util.stream.Collectors;

public class SatuSehatSearchLocationsAsRoomsEndpoint implements SatuSehatEndpoint<StandardizedResourceResponse<SatuSehatSearchLocationResponse>> {

  private String authToken;
  private String organizationId;

  @Builder
  public SatuSehatSearchLocationsAsRoomsEndpoint(String organizationId) {
    this.organizationId = organizationId;
  }

  @Override
  public ResponseEntity<StandardizedResourceResponse<SatuSehatSearchLocationResponse>> performHttpRequest() {
    RestClient restClient = SatuSehatRestClient.getRestClient(this.authToken);

    ResponseEntity<StandardizedResourceResponse<SatuSehatSearchLocationResponse>> response = restClient.method(HttpMethod.GET)
        .uri(uriBuilder -> uriBuilder
            .path(SatuSehatLocationPath.BASE_PATH)
            .queryParam("organization", this.organizationId)
            .build())
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {});
    response.getBody().setEntries(
        response.getBody().getEntries()
            .stream()
            .filter(r -> r.getResource()
                .getIdentifier()
                .stream()
                .anyMatch(i -> Optional.ofNullable(i.getSystem()).map(s -> s.contains(organizationId)).orElse(false))
            )
            .filter(r -> r.getResource()
                .getPhysicalType()
                .getCoding()
                .stream()
                .anyMatch(l -> l.getCode().equals(LocationPhysicalTypeEnum.RO.getCode()))
            )
            .collect(Collectors.toList()));
    return response;
  }

  @Override
  public SatuSehatEndpoint<StandardizedResourceResponse<SatuSehatSearchLocationResponse>> setAuthToken(String authToken) {
    this.authToken = authToken;
    return this;
  }
}


