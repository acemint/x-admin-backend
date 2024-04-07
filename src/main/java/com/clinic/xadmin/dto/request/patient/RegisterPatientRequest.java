package com.clinic.xadmin.dto.request.patient;

import com.clinic.xadmin.validator.annotation.ValidEmail;
import com.clinic.xadmin.validator.annotation.ValidGender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class RegisterPatientRequest {

  @NotNull
  private String firstName;

  private String lastName;

  @Min(18)
  private int age;

  @ValidGender
  private String gender;

  @ValidEmail
  private String emailAddress;

  @NotNull
  private String address;

  @NotNull
  private String phoneNumber;

  @NotNull
  private String clinicCode;

}
