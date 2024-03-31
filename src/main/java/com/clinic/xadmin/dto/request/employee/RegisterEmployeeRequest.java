package com.clinic.xadmin.dto.request.employee;

import com.clinic.xadmin.validator.annotation.ValidDoctorNumberNotNullOnSpecificRole;
import com.clinic.xadmin.validator.annotation.ValidEmail;
import com.clinic.xadmin.validator.annotation.ValidGender;
import com.clinic.xadmin.validator.annotation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@ValidDoctorNumberNotNullOnSpecificRole
public class RegisterEmployeeRequest {

  @NotNull
  private String firstName;

  private String lastName;

  @Min(18)
  private int age;

  @ValidGender
  private String gender;

  private String doctorNumber;

  @ValidEmail
  private String emailAddress;

  @NotNull
  private String status;
  private String role;
  private String type;

  @NotNull
  private String address;

  @NotNull
  private String phoneNumber;

  @ValidPassword
  private String password;

}
