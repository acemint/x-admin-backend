package com.clinic.xadmin.controller.helper;

import com.clinic.xadmin.constant.employee.EmployeeRole;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.exception.XAdminIllegalStateException;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ControllerHelperImpl implements ControllerHelper {

  private final AppSecurityContextHolder appSecurityContextHolder;
  private final ClinicRepository clinicRepository;

  @Autowired
  public ControllerHelperImpl(AppSecurityContextHolder appSecurityContextHolder, ClinicRepository clinicRepository) {
    this.appSecurityContextHolder = appSecurityContextHolder;
    this.clinicRepository = clinicRepository;
  }

  @Override
  @Nonnull public Clinic getInjectableClinicFromAuthentication(@Nullable String clinicCode) {
    Authentication authentication = this.appSecurityContextHolder.getCurrentContext().getAuthentication();
    Employee employee = ((CustomUserDetails) authentication.getPrincipal()).getEmployee();
    if (!isFirefighterRoles(employee)) {
      return this.getClinicFromNonFireFighterRoles(employee);
    }
    else {
      return this.getClinicFromFireFighterRoles(clinicCode);
    }
  }

  private Clinic getClinicFromNonFireFighterRoles(Employee employee) {
    if (Objects.isNull(employee.getClinic())) {
      throw new XAdminIllegalStateException("Unable to get user role " + employee.getRole() + " without clinic id");
    }
    return employee.getClinic();
  }

  private Clinic getClinicFromFireFighterRoles(String clinicCode) {
    if (Objects.isNull(clinicCode)) {
      throw new XAdminIllegalStateException("The corresponding controller should not be hit with Firefighter Roles, request for changes if needed");
    }
    Clinic existingClinic = this.clinicRepository.searchByCode(clinicCode);
    if (Objects.isNull(existingClinic)) {
      throw new XAdminBadRequestException("Clinic code passed is invalid");
    }
    return existingClinic;
  }

  private static Boolean isFirefighterRoles(Employee employee) {
    return EmployeeRole.FIREFIGHTER_ROLES.containsKey(employee.getRole());
  }

}
