package com.clinic.xadmin.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SessionManagementConfiguration {

  public static final String DEFAULT_SESSION_MANAGEMENT_REPOSITORY_BEAN_NAME = "SesssionManagementRepositoryBean";

  @Bean(value = DEFAULT_SESSION_MANAGEMENT_REPOSITORY_BEAN_NAME)
  public SecurityContextRepository securityContextRepository() {
    return new DelegatingSecurityContextRepository(
        new HttpSessionSecurityContextRepository());
  }
}
