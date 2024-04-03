package com.clinic.xadmin.service.employee;

import com.clinic.xadmin.constant.employee.EmployeeRole;
import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.dto.request.employee.ResetPasswordRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.exception.XAdminInternalException;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.security.authprovider.CustomUserDetails;
import com.clinic.xadmin.service.helper.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final PasswordEncoder passwordEncoder;
  private final ServiceHelper serviceHelper;
  private final ClinicRepository clinicRepository;

  @Autowired
  private EmployeeServiceImpl(EmployeeRepository employeeRepository,
      PasswordEncoder passwordEncoder,
      ServiceHelper serviceHelper,
      ClinicRepository clinicRepository) {
    this.employeeRepository = employeeRepository;
    this.passwordEncoder = passwordEncoder;
    this.serviceHelper = serviceHelper;
    this.clinicRepository = clinicRepository;
  }

  @Override
  public Employee createEmployee(RegisterEmployeeRequest request) {
    Clinic clinic = this.serviceHelper.getClinicFromAuthentication();
    Employee existingEmployee = this.employeeRepository.findEmployeeByEmailAddress(clinic.getId(), request.getEmailAddress());
    if (!Objects.isNull(existingEmployee)) {
      throw new XAdminBadRequestException("Email has been taken");
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
    Clinic clinic = this.serviceHelper.getClinicFromAuthentication();
    employeeFilter.setClinicId(clinic.getId());

    return this.employeeRepository.findByFilter(employeeFilter);
  }

  @Override
  public Employee resetPassword(ResetPasswordRequest request) {
    Clinic clinic = this.serviceHelper.getClinicFromAuthentication();
    Employee existingEmployee = this.employeeRepository.findEmployeeByEmailAddress(clinic.getId(), request.getEmailAddress());

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
