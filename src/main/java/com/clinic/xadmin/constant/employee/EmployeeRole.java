package com.clinic.xadmin.constant.employee;

import com.clinic.xadmin.entity.Employee;

import java.util.Map;

public class EmployeeRole {

  public static final String ROLE_DEVELOPER = "ROLE_DEVELOPER";
  public static final String ROLE_IT_HELPDESK = "ROLE_IT_HELPDESK";
  public static final String ROLE_DOCTOR = "ROLE_DOCTOR";
  public static final String ROLE_CLINIC_ADMIN = "ROLE_CLINIC_ADMIN";
  public static final String ROLE_REGULAR_EMPLOYEE = "ROLE_REGULAR_EMPLOYEE";

  // Firefighter roles are defined as roles which does not need any clinic
  private static final Map<String, Boolean> FIREFIGHTER_ROLES = Map.ofEntries(
      Map.entry(ROLE_DEVELOPER, true),
      Map.entry(ROLE_IT_HELPDESK, true)
  );

  public static Boolean isFirefighterRoles(Employee employee) {
    return EmployeeRole.FIREFIGHTER_ROLES.containsKey(employee.getRole());
  }


}
