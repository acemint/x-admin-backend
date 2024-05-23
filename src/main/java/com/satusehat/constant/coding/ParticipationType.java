package com.satusehat.constant.coding;

import lombok.Getter;

@Getter
public class ParticipationType {

  private final String system = "http://terminology.hl7.org/CodeSystem/v3-ParticipationType";
  private final String code;
  private final String display;

  public ParticipationType(ParticipationTypeEnum participationTypeEnum) {
    this.code = participationTypeEnum.getCode();
    this.display = participationTypeEnum.getDisplayName();
  }

}
