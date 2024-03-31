package com.clinic.xadmin.helper;

import com.clinic.xadmin.constant.EmployeeRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class WithMockCustomUserConstants {

  public static final String DEFAULT_USERNAME = "username";
  public static final String DEFAULT_ROLES = EmployeeRole.ROLE_REGULAR_EMPLOYEE;
  public static final String DEFAULT_CLINIC_ID = "123";
}
