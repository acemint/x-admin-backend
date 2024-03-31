package com.clinic.xadmin.security.role;

import com.clinic.xadmin.constant.EmployeeRole;
import com.clinic.xadmin.controller.constant.SecurityAuthorizationType;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component(value = AuthorizationEvaluator.BEAN_NAME)
public class AuthorizationEvaluator  {

  public static final String BEAN_NAME = "authorizationEvaluator";
  private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

  private final AppSecurityContextHolder appSecurityContextHolder;

  @Autowired
  public AuthorizationEvaluator(AppSecurityContextHolder appSecurityContextHolder) {
    this.appSecurityContextHolder = appSecurityContextHolder;
  }

  public boolean hasRole(String permission) {
    Authentication authentication = this.appSecurityContextHolder.getCurrentContext().getAuthentication();

    if (authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains(ROLE_ANONYMOUS)) {
      return false;
    }
    Employee employee = ((CustomUserDetails) authentication.getPrincipal()).getEmployee();
    return validateByRoleBase(employee, permission);
  }

  private boolean validateByRoleBase(Employee employee, String permission) {
    if (employee.getRole().equals(EmployeeRole.ROLE_DEVELOPER)) {
      return true;
    }

    Clinic clinic = employee.getClinic();
    // TODO: set clinic subscription to a certain timeframe, now will bypass all clinic
    if (!Objects.isNull(clinic.getSubscriptionValidTo())) {
      if (clinic.getSubscriptionValidTo().isBefore(LocalDateTime.now())) {
        return false;
      }
    }

    return Arrays.stream(permission.split(SecurityAuthorizationType.ROLE_SPLITTER)).toList()
        .contains(employee.getRole());
  }

  private boolean isContainRole(Employee employee, List<String> roles) {
    if (roles.contains(employee.getRole())) {
      return true;
    }
    return false;
  }

}
