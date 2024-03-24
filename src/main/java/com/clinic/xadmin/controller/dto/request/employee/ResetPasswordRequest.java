package com.clinic.xadmin.controller.dto.request.employee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class ResetPasswordRequest {

  private String emailAddress;
  private String previousPassword;
  private String newPassword;

}
