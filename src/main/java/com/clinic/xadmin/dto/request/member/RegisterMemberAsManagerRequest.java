package com.clinic.xadmin.dto.request.member;

import com.clinic.xadmin.validator.annotation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

  @ValidPassword
  private String password;

}
