package com.satusehat.property;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

public class SatuSehatPropertyFactory {

  public static SatuSehatProperty createFrom(PropertyDefinition propertyDefinition) {

    YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
    factoryBean.setResources(new ClassPathResource(propertyDefinition.constructFullFilePath()));

    Properties properties = factoryBean.getObject();

    ConfigurationPropertySource propertySource = new MapConfigurationPropertySource(properties);
    Binder binder = new Binder(propertySource);

    return binder.bind("satusehat.integration", SatuSehatProperty.class).get();
  }

}
