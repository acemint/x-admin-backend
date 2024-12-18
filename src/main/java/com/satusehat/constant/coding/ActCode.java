package com.satusehat.constant.coding;

import lombok.Data;

@Data
public class ActCode {

  private final String system = "http://terminology.hl7.org/CodeSystem/v3-ActCode";
  private String code;
  private String display;

  public ActCode(ActCodeEnum actCodeEnum) {
    this.code = actCodeEnum.getCode();
    this.display = actCodeEnum.getDisplayName();
  }

}
