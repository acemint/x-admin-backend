package com.clinic.xadmin.dto.request.member;

import com.clinic.xadmin.validator.annotation.ValidEmail;
import com.clinic.xadmin.validator.annotation.ValidGender;
import com.clinic.xadmin.validator.annotation.ValidPassword;
import com.clinic.xadmin.validator.annotation.ValidRoleToRegister;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldNameConstants
public class RegisterMemberRequest {

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
  private String status;

  @ValidRoleToRegister
  private String role;

  @NotNull
  private String address;

  @NotNull
  private String phoneNumber;

  @ValidPassword
  private String password;

}
