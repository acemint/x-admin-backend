package com.clinic.xadmin.constant.employee;

import java.util.List;
import java.util.Map;

public class EmployeeRole {

  public static final String ROLE_DEVELOPER = "ROLE_DEVELOPER";
  public static final String ROLE_IT_HELPDESK = "ROLE_IT_HELPDESK";
  public static final String ROLE_DOCTOR = "ROLE_DOCTOR";
  public static final String ROLE_CLINIC_ADMIN = "ROLE_CLINIC_ADMIN";
  public static final String ROLE_REGULAR_EMPLOYEE = "ROLE_REGULAR_EMPLOYEE";

  public static final Map<String, Boolean> LIST_ROLE_WITHOUT_CLINIC_IDS = Map.ofEntries(
      Map.entry(ROLE_DEVELOPER, true),
      Map.entry(ROLE_IT_HELPDESK, true)
  );

}
