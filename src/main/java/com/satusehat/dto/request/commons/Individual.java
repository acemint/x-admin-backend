package com.satusehat.dto.request.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.satusehat.constant.ResourceType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Individual {

  @JsonProperty(value = "reference")
  private String satuSehatReferenceId;

  @JsonProperty(value = "display")
  private String fullName;

  public static class IndividualBuilder {

    public IndividualBuilder satuSehatReferenceId(ResourceType resourceType, String referenceId) {
      this.satuSehatReferenceId = resourceType.getValue() + "/" + referenceId;
      return this;
    }

  }

}
