package com.clinic.xadmin.dto.response.visit;

import com.clinic.xadmin.dto.response.member.MemberResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitResponse {

  private String code;
  private String status;
  private MemberResponse patient;
  private MemberResponse practitioner;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

}
