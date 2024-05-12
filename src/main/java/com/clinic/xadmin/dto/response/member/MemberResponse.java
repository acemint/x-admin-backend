package com.clinic.xadmin.dto.response.member;

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
public class MemberResponse {

  private String clinicUsername;
  private String code;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private String phoneNumber;
  private String practitionerType;
  private String role;
  private String status;

}
