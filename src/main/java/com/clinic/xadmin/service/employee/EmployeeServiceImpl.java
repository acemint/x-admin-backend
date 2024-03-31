package com.clinic.xadmin.service.employee;

import com.clinic.xadmin.constant.EmployeeRole;
import com.clinic.xadmin.constant.EmployeeType;
import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.dto.request.employee.ResetPasswordRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import com.clinic.xadmin.service.exception.XAdminBadRequestException;
import com.clinic.xadmin.service.exception.XAdminInternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final PasswordEncoder passwordEncoder;
  private final AppSecurityContextHolder appSecurityContextHolder;
  private final ClinicRepository clinicRepository;

  @Autowired
  private EmployeeServiceImpl(EmployeeRepository employeeRepository,
      PasswordEncoder passwordEncoder,
      AppSecurityContextHolder appSecurityContextHolder,
      ClinicRepository clinicRepository) {
    this.employeeRepository = employeeRepository;
    this.passwordEncoder = passwordEncoder;
    this.appSecurityContextHolder = appSecurityContextHolder;
    this.clinicRepository = clinicRepository;
  }

  @Override
  public Employee createEmployee(RegisterEmployeeRequest request) {
    Employee existingEmployee = this.employeeRepository.findEmployeeByEmailAddress(request.getEmailAddress());
    if (!Objects.isNull(existingEmployee)) {
      throw new XAdminBadRequestException("Email has been taken");
    }

    Clinic clinic = this.findClinicFromAuth();
    if (Objects.isNull(clinic)) {
      throw new XAdminInternalException("Clinic not found from authentication object");
    }

    // TODO: Move to Jakarta Bean Validation
    if (request.getRole().equals(EmployeeRole.ROLE_REGULAR_EMPLOYEE)) {
      throw new XAdminBadRequestException("Unsupported role");
    }

    Employee employee = Employee.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .emailAddress(request.getEmailAddress())
        .phoneNumber(request.getPhoneNumber())
        .password(this.passwordEncoder.encode(request.getPassword()))
        .type(request.getType())
        .role(request.getRole())
        .status(request.getStatus())
        .clinic(clinic)
        .build();
    return this.employeeRepository.save(employee);
  }

  @Override
  public Page<Employee> getEmployees(EmployeeFilter employeeFilter) {
    Authentication authentication = this.appSecurityContextHolder.getCurrentContext().getAuthentication();
    Employee employee = ((CustomUserDetails) authentication.getPrincipal()).getEmployee();

    if (!employee.getRole().equals(EmployeeRole.ROLE_DEVELOPER)) {
      employeeFilter.setClinicId(employee.getClinic().getId());
    }
    return this.employeeRepository.findByFilter(employeeFilter);
  }

  @Override
  public Employee resetPassword(ResetPasswordRequest request) {
    Employee existingEmployee = this.employeeRepository.findEmployeeByEmailAddress(request.getEmailAddress());
    if (Objects.isNull(existingEmployee)) {
      throw new XAdminBadRequestException("User not found");
    }

    if (this.passwordEncoder.matches(request.getPreviousPassword(), existingEmployee.getPassword())) {
      throw new XAdminBadRequestException("Previous password does not match");
    }

    existingEmployee.setPassword(this.passwordEncoder.encode(request.getNewPassword()));
    return this.employeeRepository.save(existingEmployee);
  }

  private Clinic findClinicFromAuth() {
    Authentication authentication = this.appSecurityContextHolder.getCurrentContext().getAuthentication();
    Employee employee = ((CustomUserDetails) authentication.getPrincipal()).getEmployee();
    return employee.getClinic();
  }

}
