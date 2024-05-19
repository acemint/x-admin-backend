package com.satusehat.dto.response.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
//TODO: Might be removed, because the POST endpoint sometimes return the resourceID and sometimes refer to duplicate
public class PatientCreationResourceErrorResponse {

  private Boolean success;
  private String message;

  @JsonProperty(value = "data")
  private Response content;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    @JsonProperty(value = "resourceID")
    private String patientId;

  }


}
