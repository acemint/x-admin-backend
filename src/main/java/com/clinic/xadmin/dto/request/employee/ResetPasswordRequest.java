package com.clinic.xadmin.dto.request.employee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class ResetPasswordRequest {

  private String username;
  private String previousPassword;
  private String newPassword;

}
