package com.clinic.xadmin.dto.request.member;

import com.clinic.xadmin.validator.annotation.ValidEmail;
import com.clinic.xadmin.validator.annotation.ValidGender;
import com.clinic.xadmin.validator.annotation.ValidStatus;
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

  @NotNull
  @ValidGender
  private String gender;

  @NotNull
  @ValidEmail
  private String emailAddress;

  @NotNull
  @ValidStatus
  private String status;

  private String address;

  private String phoneNumber;

}
