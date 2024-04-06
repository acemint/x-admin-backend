package com.clinic.xadmin.dto.response.employee;

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
public class EmployeeResponse {

  private String id;
  private String username;
  private String code;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private String phoneNumber;
  private String type;
  private String role;
  private String status;

}
