package com.clinic.xadmin.dto.request.member;

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
public class RegisterMemberAsPractitionerRequest extends RegisterMemberRequest {

  @NotNull
  private String nik;

  private String practitionerType;

  @NotNull
  private String practitionerPracticeLicense;

  private String practitionerSalary;

  private String practitionerTaxPercentage;

}
