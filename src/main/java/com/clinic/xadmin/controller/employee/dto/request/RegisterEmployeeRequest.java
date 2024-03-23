package com.clinic.xadmin.controller.employee.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class RegisterEmployeeRequest {

  private String firstName;
  private String lastName;
  private int age;
  private String gender;
  private String doctorNumber;
  private String email;
  private String status;
  private String role;
  private String type;
  private String address;
  private String phoneNumber;
  private String password;

}
