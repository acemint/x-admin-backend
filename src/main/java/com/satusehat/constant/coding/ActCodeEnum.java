package com.satusehat.constant.coding;

public enum ActCodeEnum {

  AMB("AMB", "ambulatory"),
  EMER("EMER", "emergency");

  private String code;
  private String displayName;

  ActCodeEnum(String code, String displayName) {
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