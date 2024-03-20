package com.clinic.xadmin.service.employee;

import com.clinic.xadmin.constant.EmployeeRole;
import com.clinic.xadmin.constant.EmployeeStatus;
import com.clinic.xadmin.constant.EmployeeType;
import com.clinic.xadmin.controller.security.dto.request.RegisterEmployeeRequest;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.repository.employee.EmployeeRepository;
import com.clinic.xadmin.service.exception.XAdminBadRequestException;
import jakarta.validation.constraints.Email;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  private EmployeeServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
    this.employeeRepository = employeeRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Employee createEmployee(RegisterEmployeeRequest request) {
    if (!EmailValidator.getInstance().isValid(request.getEmail())) {
      throw new XAdminBadRequestException("Email format is invalid");
    }

    Employee existingEmployee = this.employeeRepository.findEmployeeByEmailAddress(request.getEmail());
    if (!Objects.isNull(existingEmployee)) {
      throw new XAdminBadRequestException("Email has been taken");
    }

    if (request.getType().equals(EmployeeType.DOCTOR)
        && !StringUtils.isEmpty(request.getDoctorNumber())) {
      throw new XAdminBadRequestException("Doctor number should be inputted");
    }

    if (request.getRole().equals(EmployeeRole.ROLE_DEVELOPER)) {
      throw new XAdminBadRequestException("Unsupported role");
    }

    Employee employee = Employee.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .emailAddress(request.getEmail())
        .phoneNumber(request.getPhoneNumber())
        .password(this.passwordEncoder.encode(request.getPassword()))
        .type(request.getType())
        .role(request.getRole())
        .status(request.getStatus())
        .build();
    return this.employeeRepository.save(employee);
  }

}
