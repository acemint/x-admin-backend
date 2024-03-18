package com.clinic.xadmin.controller.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginEmployeeRequest {

  private String email;
  private String password;

}
