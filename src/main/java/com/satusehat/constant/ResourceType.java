package com.satusehat.constant;

public enum ResourceType {

  ORGANIZATION("Organization"),
  PRACTITIONER("Practitioner"),
  LOCATION("Location"),
  PATIENT("Patient");

  private final String value;

  ResourceType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
