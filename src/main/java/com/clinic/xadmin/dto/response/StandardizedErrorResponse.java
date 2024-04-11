package com.clinic.xadmin.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StandardizedErrorResponse {

  private String xAdminErrorCode;
  private String message;
  private Map<String, String> fields;


}
