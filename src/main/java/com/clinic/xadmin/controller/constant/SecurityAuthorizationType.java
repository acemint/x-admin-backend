package com.clinic.xadmin.controller.constant;

import com.clinic.xadmin.constant.EmployeeRole;

public class SecurityAuthorizationType {

  public static final String IS_FULLY_AUTHENTICATED = "isFullyAuthenticated()";

  public static final String HAS_PERMISSION_FUNCTION_PREFIX = "hasPermission(authentication, 'null', 'null', '";
  public static final String ROLE_BASED_PERMISSION_PREFIX = "RoleBasedValidation::";
  public static final String IS_DEVELOPER =
      HAS_PERMISSION_FUNCTION_PREFIX + ROLE_BASED_PERMISSION_PREFIX + EmployeeRole.ROLE_DEVELOPER + "')";
  public static final String IS_ADMIN_OR_DEVELOPER =
      HAS_PERMISSION_FUNCTION_PREFIX + ROLE_BASED_PERMISSION_PREFIX + EmployeeRole.ROLE_DEVELOPER + "_" + EmployeeRole.ROLE_ADMIN + "')";

}
