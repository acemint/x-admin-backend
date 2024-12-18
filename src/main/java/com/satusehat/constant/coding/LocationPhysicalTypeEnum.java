package com.satusehat.constant.coding;

public enum LocationPhysicalTypeEnum {

  RO("ro", "room"),
  SI("si", "site"),
  BU("bu", "building"),
  VI("vi", "virtual");

  private final String code;
  private final String displayName;

  LocationPhysicalTypeEnum(String code, String displayName) {
    this.code = code;
    this.displayName = displayName;
  }

  public String getCode() {
    return this.code;
  }

  public String getDisplayName() {
    return this.displayName;
  }

}