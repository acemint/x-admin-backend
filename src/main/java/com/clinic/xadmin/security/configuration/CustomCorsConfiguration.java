package com.clinic.xadmin.security.configuration;

import com.clinic.xadmin.security.property.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CustomCorsConfiguration {

  public static final String CORS_CONFIGURATION_BEAN_NAME = "CorsConfiguration";
  private final SecurityProperties securityProperties;

  @Autowired
  public CustomCorsConfiguration(SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  @Bean(value = CustomCorsConfiguration.CORS_CONFIGURATION_BEAN_NAME)
  public CorsConfigurationSource corsConfiguration() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowCredentials(Boolean.TRUE);
    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));
    corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
    corsConfiguration.setAllowedOriginPatterns(securityProperties.getCors().getAllowedHosts());

    UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }

}
