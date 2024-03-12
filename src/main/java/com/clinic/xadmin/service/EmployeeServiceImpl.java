package com.clinic.xadmin.service;

import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private EmployeeRepository employeeRepository;

  @Autowired
  public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public Employee getEmployee() {
    return this.employeeRepository.getEmployeeById();
  }
}
