package com.clinic.xadmin.security.constant;

import com.clinic.xadmin.constant.employee.EmployeeRole;
import com.clinic.xadmin.security.role.AuthorizationEvaluator;

public class SecurityAuthorizationType {

  public static final String IS_FULLY_AUTHENTICATED = "isFullyAuthenticated()";
  public static final String ROLE_SPLITTER = "::";


  private static final String HAS_PERMISSION_FUNCTION_PREFIX = "@" + AuthorizationEvaluator.BEAN_NAME + ".hasRole('";
  private static final String HAS_PERMISSION_FUNCTION_SUFFIX = "')";

  public static final String IS_CLINIC_ADMIN = HAS_PERMISSION_FUNCTION_PREFIX + EmployeeRole.ROLE_CLINIC_ADMIN + HAS_PERMISSION_FUNCTION_SUFFIX;
  public static final String IS_DEVELOPER = HAS_PERMISSION_FUNCTION_PREFIX + EmployeeRole.ROLE_DEVELOPER + HAS_PERMISSION_FUNCTION_SUFFIX;

}
