package com.clinic.xadmin.service.helper;

import com.clinic.xadmin.constant.employee.EmployeeRole;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    return employee.getClinic();
  }

}
