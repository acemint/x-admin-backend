package com.clinic.xadmin.dto.request.member;

import com.clinic.xadmin.validator.annotation.ValidPassword;
import com.clinic.xadmin.validator.annotation.ValidRegisterManagerRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class RegisterMemberAsManagerRequest extends RegisterMemberRequest {

  @NotNull
  @ValidPassword
  private String password;

  @NotNull
  @ValidRegisterManagerRole
  private String role;

}
