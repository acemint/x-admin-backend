package com.satusehat.endpoint.oauth;

import com.satusehat.dto.response.oauth.OAuthResponse;
import com.satusehat.endpoint.BaseSatuSehatEndpoint;
import com.satusehat.property.SatuSehatPropertyHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class SatuSehatOauthEndpoint implements BaseSatuSehatEndpoint<OAuthResponse> {

  private static final String PATH = "/accesstoken";
  private static final Map.Entry<String, Object> QUERY_PARAM_GRANT_TYPE = Map.entry("grant_type", "client_credentials");
  private static final String HTTP_METHOD = "POST";

  private final String clientId;
  private final String clientSecret;

  public SatuSehatOauthEndpoint(String clientId, String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  @Override
  public ResponseEntity<OAuthResponse> getMethodCall() {
    RestClient restClient = RestClient.builder()
        .baseUrl(SatuSehatPropertyHolder.getInstance().getAuthUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    return restClient.method(HttpMethod.valueOf(HTTP_METHOD))
        .uri(uriBuilder -> uriBuilder
            .path(PATH)
            .queryParam(QUERY_PARAM_GRANT_TYPE.getKey(), QUERY_PARAM_GRANT_TYPE.getValue())
            .build())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(this.buildRequestBody())
        .retrieve()
        .toEntity(OAuthResponse.class);
  }

  @Override
  public BaseSatuSehatEndpoint<OAuthResponse> setAuthToken(String authToken) {
    return null;
  }

  private MultiValueMap<String, String> buildRequestBody() {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("client_id", clientId);
    form.add("client_secret", clientSecret);
    return form;
  }
}
