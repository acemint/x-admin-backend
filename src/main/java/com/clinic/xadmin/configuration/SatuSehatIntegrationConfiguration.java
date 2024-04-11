package com.clinic.xadmin.configuration;

import com.clinic.xadmin.property.SatuSehatIntegrationProperty;
import com.satusehat.property.PropertyDefinition;
import com.satusehat.property.SatuSehatProperty;
import com.satusehat.property.SatuSehatPropertyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SatuSehatIntegrationConfiguration {

  private final SatuSehatIntegrationProperty satuSehatIntegrationProperty;

  @Autowired
  public SatuSehatIntegrationConfiguration(SatuSehatIntegrationProperty satuSehatIntegrationProperty) {
    this.satuSehatIntegrationProperty = satuSehatIntegrationProperty;
  }

  @Bean
  public SatuSehatProperty satuSehatProperty() {
    return SatuSehatPropertyFactory.createFrom(PropertyDefinition.withDefaults().setEnvironment(
        satuSehatIntegrationProperty.getEnvironment()));
  }
}
