package com.clinic.xadmin.security.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "xadmin.security")
public class SecurityProperties {

  private CorsProperties cors;

  @Data
  public static class CorsProperties {
    private List<String> allowedHosts;
  }

}
