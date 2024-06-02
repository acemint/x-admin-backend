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

  @JsonProperty(value = "data")
  private Response content;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    @JsonProperty(value = "patient_id")
    private String patientId;

  }


}
