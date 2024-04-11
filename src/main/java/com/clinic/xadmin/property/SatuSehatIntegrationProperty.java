package com.clinic.xadmin.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("xadmin.satusehat.integration")
public class SatuSehatIntegrationProperty {

  private String environment;

}
