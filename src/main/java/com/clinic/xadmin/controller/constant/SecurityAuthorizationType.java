package com.clinic.xadmin.controller.constant;

public class SecurityAuthorizationType {

  public static final String IS_FULLY_AUTHENTICATED = "isFullyAuthenticated()";
  public static final String IS_DEVELOPER = "hasAnyRole('DEVELOPER')";
  public static final String IS_ADMIN_OR_DEVELOPER = "hasAnyRole('DEVELOPER', 'ADMIN')";

}
