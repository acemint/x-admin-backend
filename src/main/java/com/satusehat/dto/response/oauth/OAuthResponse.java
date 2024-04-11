package com.satusehat.dto.response.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthResponse {

  @JsonProperty("organization_name")
  private String organizationName;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("issued_at")
  private String issuedAt;

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("developer")
  private DeveloperResponse developer;

  @JsonProperty("scope")
  private String scope;

  @JsonProperty("expires_in")
  private String expiresIn;

  @JsonProperty("refresh_count")
  private String refreshCount;

  @JsonProperty("status")
  private String status;


  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DeveloperResponse {

    @JsonProperty("email")
    private String email;

  }

}
