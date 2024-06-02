package com.satusehat.constant.coding;

public enum ParticipationTypeEnum {

  ATND("ATND", "attender");

  private final String code;
  private final String displayName;

  ParticipationTypeEnum(String code, String displayName) {
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