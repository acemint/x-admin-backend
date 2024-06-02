package com.clinic.xadmin.dto.request.member;

import com.clinic.xadmin.validator.annotation.member.ValidRegisterPatientNikAndMotherNik;
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
@ValidRegisterPatientNikAndMotherNik
public class RegisterMemberAsPatientRequest extends RegisterMemberRequest {

  private String nik;
  private String mothersNik;

}
