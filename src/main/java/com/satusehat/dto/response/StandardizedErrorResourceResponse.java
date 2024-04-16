package com.satusehat.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardizedErrorResourceResponse {

  private String resourceType;
  private List<Issue> issue;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Issue {

    private String severity;
    private String code;
    private Detail details;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {

      private String text;

    }

  }

}
