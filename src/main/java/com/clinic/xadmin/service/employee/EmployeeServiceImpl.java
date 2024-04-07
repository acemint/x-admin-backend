package com.clinic.xadmin.service.employee;

import com.clinic.xadmin.dto.request.employee.ResetPasswordRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.mapper.EmployeeMapper;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import com.clinic.xadmin.model.employee.RegisterEmployee;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final PasswordEncoder passwordEncoder;
  private final ClinicRepository clinicRepository;

  @Autowired
  private EmployeeServiceImpl(EmployeeRepository employeeRepository,
      PasswordEncoder passwordEncoder,
      ClinicRepository clinicRepository) {
    this.employeeRepository = employeeRepository;
    this.passwordEncoder = passwordEncoder;;
    this.clinicRepository = clinicRepository;
  }

  @Override
  public Employee createEmployee(RegisterEmployee registerEmployee) {
    Employee existingEmployee = this.employeeRepository.searchByClinicCodeAndEmailAddress(registerEmployee.getClinicCode(), registerEmployee.getEmailAddress());
    if (Objects.nonNull(existingEmployee)) {
      throw new XAdminBadRequestException("Email has been taken");
    }
    Clinic clinic = this.clinicRepository.searchByCode(registerEmployee.getClinicCode());

    Employee employee = EmployeeMapper.INSTANCE.createFrom(registerEmployee);
    employee.setPassword(this.passwordEncoder.encode(registerEmployee.getPassword()));
    employee.setUsername(this.getValidUsername(registerEmployee, clinic, null));
    employee.setCode(this.employeeRepository.getNextCode());
    employee.setClinic(clinic);

    return this.employeeRepository.save(employee);
  }

  private String getValidUsername(RegisterEmployee registerEmployee, Clinic clinic, Integer additionalIndex) {
    StringBuilder username = new StringBuilder().append(registerEmployee.getFirstName().toLowerCase());
    if (!StringUtils.hasText(registerEmployee.getLastName())) {
      username.append(".").append(registerEmployee.getFirstName().toLowerCase());
    }
    if (Objects.nonNull(additionalIndex)) {
      username.append(additionalIndex);
    }
    username.append("@").append(clinic.getCode().toLowerCase().split("-")[1]);

    if (Objects.nonNull(this.employeeRepository.searchByUsername(username.toString()))) {
      return this.getValidUsername(registerEmployee, clinic, Optional.ofNullable(additionalIndex).map(i -> i + 1).orElse(1));
    }
    return username.toString();
  }

  @Override
  public Page<Employee> getEmployees(EmployeeFilter employeeFilter) {
    employeeFilter.setClinicCode(employeeFilter.getClinicCode());

    return this.employeeRepository.searchByFilter(employeeFilter);
  }

  @Override
  public Employee resetPassword(ResetPasswordRequest request) {
    Employee existingEmployee = this.employeeRepository.searchByUsername(request.getUsername());

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
