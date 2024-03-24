package com.clinic.xadmin.controller.dto.request.employee;

import com.clinic.xadmin.validator.annotation.ValidEmail;
import com.clinic.xadmin.validator.annotation.ValidPassword;
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
  @ValidEmail private String emailAddress;
  private String status;
  private String role;
  private String type;
  private String address;
  private String phoneNumber;
  @ValidPassword  private String password;

}
