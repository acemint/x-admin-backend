package com.clinic.xadmin.configuration;

import com.clinic.xadmin.property.SatuSehatIntegrationProperty;
import com.satusehat.property.PropertyDefinition;
import com.satusehat.property.SatuSehatProperty;
import com.satusehat.property.SatuSehatPropertyFactory;
import com.satusehat.property.SatuSehatPropertyHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@Slf4j
public class SatuSehatIntegrationConfiguration {

  private final SatuSehatIntegrationProperty satuSehatIntegrationProperty;

  @Autowired
  public SatuSehatIntegrationConfiguration(SatuSehatIntegrationProperty satuSehatIntegrationProperty) {
    this.satuSehatIntegrationProperty = satuSehatIntegrationProperty;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void satuSehatProperty() {
    SatuSehatProperty satuSehatProperty = SatuSehatPropertyFactory.createFrom(PropertyDefinition.withDefaults().setEnvironment(
        satuSehatIntegrationProperty.getEnvironment()));
    SatuSehatPropertyHolder.initialize(satuSehatProperty);
    log.info("Satu Sehat Property: satusehat-{}.yaml Initialized", satuSehatIntegrationProperty.getEnvironment());
  }
}
