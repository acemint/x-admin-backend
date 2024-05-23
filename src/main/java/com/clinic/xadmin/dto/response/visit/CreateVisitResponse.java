package com.clinic.xadmin.dto.response.visit;

import com.clinic.xadmin.dto.response.member.MemberResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVisitResponse {

  private String code;
  private MemberResponse patient;
  private MemberResponse practitioner;

}
