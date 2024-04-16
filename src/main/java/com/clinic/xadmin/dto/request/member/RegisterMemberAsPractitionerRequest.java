package com.clinic.xadmin.dto.request.member;

import com.clinic.xadmin.constant.member.MemberRole;
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
  private String satuSehatPractitionerReferenceId;

  private final String role = MemberRole.ROLE_PRACTITIONER;

}
