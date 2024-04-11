package com.satusehat.property;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.satusehat.constant.Environment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Setter
@Accessors(chain = true)
public class PropertyDefinition {

  @Builder.Default
  private String environment = Environment.STAGING;

  @Builder.Default
  private String fileType = "yaml";

  @Builder.Default
  private String fileName = "satusehat";

  public static PropertyDefinition withDefaults() {
    return PropertyDefinition.builder().build();
  }


  String constructFullFilePath() {
    StringBuilder propertyFileName = new StringBuilder().append(this.fileName);
    if (!Arrays.asList(Environment.VALID_ENVIRONMENTS).contains(environment)) {
      throw new RuntimeException("Invalid file");
    }
    propertyFileName.append("-").append(this.environment.toLowerCase()).append(".").append(this.fileType);
    return propertyFileName.toString();
  }

}
