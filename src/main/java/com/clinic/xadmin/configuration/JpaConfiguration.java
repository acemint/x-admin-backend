package com.clinic.xadmin.configuration;

import com.clinic.xadmin.entity.audit.AuditAware;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfiguration {

  private final AppSecurityContextHolder appSecurityContextHolder;

  @Autowired
  public JpaConfiguration(AppSecurityContextHolder appSecurityContextHolder) {
    this.appSecurityContextHolder = appSecurityContextHolder;
  }

  @Bean
  public AuditorAware<String> auditorProvider() {
    return new AuditAware(appSecurityContextHolder);
  }

}
