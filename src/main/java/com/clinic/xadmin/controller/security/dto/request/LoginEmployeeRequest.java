package com.clinic.xadmin.controller.security.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class LoginEmployeeRequest {

  private String email;
  private String password;

}
