package com.clinic.xadmin.controller.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

  private String id;
  private String email;
  private String firstName;
  private String lastName;
  private String phoneNumber;

}
