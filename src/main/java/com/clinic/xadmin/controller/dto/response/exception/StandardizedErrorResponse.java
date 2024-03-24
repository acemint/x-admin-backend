package com.clinic.xadmin.controller.dto.response.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StandardizedErrorResponse {

  private String xAdminErrorCode;
  private String message;
  private String stackTrace;

}
