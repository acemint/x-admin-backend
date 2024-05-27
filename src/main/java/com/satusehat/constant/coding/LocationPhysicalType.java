package com.satusehat.constant.coding;

import lombok.Data;

@Data
public class LocationPhysicalType {

  private final String system = "http://terminology.hl7.org/CodeSystem/location-physical-type";
  private String code;
  private String display;

  public LocationPhysicalType(LocationPhysicalTypeEnum locationPhysicalTypeEnum) {
    this.code = locationPhysicalTypeEnum.getCode();
    this.display = locationPhysicalTypeEnum.getDisplayName();
  }

}
