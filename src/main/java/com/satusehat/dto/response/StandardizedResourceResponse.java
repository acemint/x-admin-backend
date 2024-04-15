package com.satusehat.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardizedResourceResponse<T> {

  @JsonProperty("entry")
  private List<Entry<T>> entries;
  private int total;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Entry<T> {

    private String fullUrl;
    private T resource;

  }

}
