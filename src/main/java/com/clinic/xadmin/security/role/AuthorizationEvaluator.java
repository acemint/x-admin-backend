package com.clinic.xadmin.security.role;

import com.clinic.xadmin.constant.EmployeeRole;
import com.clinic.xadmin.controller.constant.SecurityAuthorizationType;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Component(value = AuthorizationEvaluator.BEAN_NAME)
public class AuthorizationEvaluator implements PermissionEvaluator {

  public static final String BEAN_NAME = "authorizationEvaluator";

  @Override
  public boolean hasPermission(Authentication authentication,
      Object targetDomainObject,
      Object permission) {
    return this.defaultHasPermission(authentication, permission);
  }

  @Override
  public boolean hasPermission(Authentication authentication,
      Serializable targetId,
      String targetType,
      Object permission) {
    return this.defaultHasPermission(authentication, permission);
  }

  private boolean defaultHasPermission(Authentication authentication, Object permission) {
    Employee employee = ((CustomUserDetails) authentication.getPrincipal()).getEmployee();
    if ( ((String) permission).startsWith(SecurityAuthorizationType.ROLE_BASED_PERMISSION_PREFIX)) {
      return validateByRoleBase(employee, permission);
    }
    return false;
  }

  private boolean validateByRoleBase(Employee employee, Object permission) {
    if (employee.getRole().equals(EmployeeRole.ROLE_DEVELOPER)) {
      return true;
    }

    Clinic clinic = employee.getClinic();
    if (clinic.getSubscriptionValidTo().isBefore(LocalDateTime.now())) {
      return false;
    }

    String rolesAsString = ((String) permission).split(SecurityAuthorizationType.ROLE_BASED_PERMISSION_PREFIX)[0];
    if ( ((String) permission).startsWith(SecurityAuthorizationType.ROLE_BASED_PERMISSION_PREFIX)) {
      return isContainRole(employee, List.of(rolesAsString.split("_")));
    }
    return false;
  }

  private boolean isContainRole(Employee employee, List<String> roles) {
    if (roles.contains(employee.getRole())) {
      return true;
    }
    return false;
  }

}
