package com.clinic.xadmin.service.helper;

import com.clinic.xadmin.constant.EmployeeRole;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ServiceHelperImpl implements ServiceHelper {

  private final AppSecurityContextHolder appSecurityContextHolder;

  @Autowired
  public ServiceHelperImpl(AppSecurityContextHolder appSecurityContextHolder) {
    this.appSecurityContextHolder = appSecurityContextHolder;
  }

  public Clinic getClinicFromAuthentication() {
    Authentication authentication = this.appSecurityContextHolder.getCurrentContext().getAuthentication();
    Employee employee = ((CustomUserDetails) authentication.getPrincipal()).getEmployee();
    /*
      while the controller already blocks that the non-developer employee should have clinic in AuthorizationEvaluator,
      this place will check again, even though it might be impossible
    */
    if (!employee.getRole().equals(EmployeeRole.ROLE_DEVELOPER)) {
      throw new IllegalStateException("Unable to get valid clinic from employee: " + employee.getEmailAddress() + " from role " + employee.getRole());
    }
    return employee.getClinic();
  }

}
