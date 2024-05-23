package com.satusehat.constant.coding;

import lombok.Getter;

@Getter
public class LocationPhysicalType {

  private final String system = "http://terminology.hl7.org/CodeSystem/location-physical-type";
  private final String code;
  private final String display;

  public LocationPhysicalType(LocationPhysicalTypeEnum locationPhysicalTypeEnum) {
    this.code = locationPhysicalTypeEnum.getCode();
    this.display = locationPhysicalTypeEnum.getDisplayName();
  }

}
