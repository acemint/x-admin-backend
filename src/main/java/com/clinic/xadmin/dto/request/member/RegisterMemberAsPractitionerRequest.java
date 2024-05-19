package com.clinic.xadmin.dto.request.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
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
