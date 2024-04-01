package com.clinic.xadmin.controller.constant;

import com.clinic.xadmin.constant.EmployeeRole;
import com.clinic.xadmin.security.role.AuthorizationEvaluator;

public class SecurityAuthorizationType {

  public static final String IS_FULLY_AUTHENTICATED = "isFullyAuthenticated()";
  public static final String ROLE_SPLITTER = "::";
  private static final String HAS_PERMISSION_FUNCTION_PREFIX = "@" + AuthorizationEvaluator.BEAN_NAME + ".hasRole('";
  public static final String IS_DEVELOPER =
      HAS_PERMISSION_FUNCTION_PREFIX +
          EmployeeRole.ROLE_DEVELOPER + "')";
  public static final String IS_ADMIN_OR_DEVELOPER =
      HAS_PERMISSION_FUNCTION_PREFIX +
          EmployeeRole.ROLE_DEVELOPER +
          ROLE_SPLITTER +
          EmployeeRole.ROLE_ADMIN + "')";

}
