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
public class PatientCreationResourceResponse {

  private Boolean success;
  private String message;

  @JsonProperty(value = "response")
  private Response content;

  @Data
  @Builder
  public static class Response {

    @JsonProperty(value = "resourceID")
    private String resourceId;

    @JsonProperty(value = "patient_id")
    private String patientId;

  }


}
