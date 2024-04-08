package com.clinic.xadmin.service.employee;

import com.clinic.xadmin.dto.request.employee.ResetPasswordRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.mapper.EmployeeMapper;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import com.clinic.xadmin.model.employee.RegisterEmployeeData;
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
  public Employee createEmployee(RegisterEmployeeData registerEmployeeData) {
    Employee existingEmployee = this.employeeRepository.searchByClinicCodeAndEmailAddress(
        registerEmployeeData.getClinicCode(), registerEmployeeData.getEmailAddress());
    if (Objects.nonNull(existingEmployee)) {
      throw new XAdminBadRequestException("Email has been taken");
    }
    Clinic clinic = this.clinicRepository.searchByCode(registerEmployeeData.getClinicCode());

    Employee employee = EmployeeMapper.INSTANCE.createFrom(registerEmployeeData);
    employee.setPassword(this.passwordEncoder.encode(registerEmployeeData.getPassword()));
    employee.setUsername(this.getValidUsername(registerEmployeeData, clinic, null));
    employee.setCode(this.employeeRepository.getNextCode());
    employee.setClinic(clinic);

    return this.employeeRepository.save(employee);
  }

  private String getValidUsername(RegisterEmployeeData registerEmployeeData, Clinic clinic, Integer additionalIndex) {
    StringBuilder username = new StringBuilder().append(registerEmployeeData.getFirstName().toLowerCase());
    if (!StringUtils.hasText(registerEmployeeData.getLastName())) {
      username.append(".").append(registerEmployeeData.getFirstName().toLowerCase());
    }
    if (Objects.nonNull(additionalIndex)) {
      username.append(additionalIndex);
    }
    username.append("@").append(clinic.getCode().toLowerCase().split("-")[1]);

    if (Objects.nonNull(this.employeeRepository.searchByUsername(username.toString()))) {
      return this.getValidUsername(registerEmployeeData, clinic, Optional.ofNullable(additionalIndex).map(i -> i + 1).orElse(1));
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
