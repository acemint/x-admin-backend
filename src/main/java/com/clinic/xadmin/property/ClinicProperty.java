package com.clinic.xadmin.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("xadmin.clinic")
public class ClinicProperty {

  private String name;

}
