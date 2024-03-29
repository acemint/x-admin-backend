package com.clinic.xadmin.service.employee;

import com.clinic.xadmin.constant.EmployeeRole;
import com.clinic.xadmin.constant.EmployeeType;
import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.dto.request.employee.ResetPasswordRequest;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.security.context.AppSecurityContextHolder;
import com.clinic.xadmin.service.exception.XAdminBadRequestException;
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

  @Autowired
  private EmployeeServiceImpl(EmployeeRepository employeeRepository,
      PasswordEncoder passwordEncoder,
      AppSecurityContextHolder appSecurityContextHolder) {
    this.employeeRepository = employeeRepository;
    this.passwordEncoder = passwordEncoder;
    this.appSecurityContextHolder = appSecurityContextHolder;
  }

  @Override
  public Employee createEmployee(RegisterEmployeeRequest request) {
    Employee existingEmployee = this.employeeRepository.findEmployeeByEmailAddress(request.getEmailAddress());
    if (!Objects.isNull(existingEmployee)) {
      throw new XAdminBadRequestException("Email has been taken");
    }

    if (Optional.ofNullable(request.getType()).map(r -> r.equals(EmployeeType.DOCTOR)).orElse(false)
        && !StringUtils.isEmpty(request.getDoctorNumber())) {
      throw new XAdminBadRequestException("Doctor number should be inputted");
    }

    if (request.getRole().equals(EmployeeRole.ROLE_DEVELOPER)) {
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

}
