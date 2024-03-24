package com.clinic.xadmin.controller.dto.response.employee;

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
  private String code;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private String phoneNumber;
  private String type;
  private String role;
  private String status;

}
