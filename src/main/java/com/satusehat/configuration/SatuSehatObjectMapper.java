package com.satusehat.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SatuSehatObjectMapper {

  public static final ObjectMapper INSTANCE;

  static {
    INSTANCE = new ObjectMapper();
    INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    INSTANCE.registerModule(new JavaTimeModule());
  }

}
