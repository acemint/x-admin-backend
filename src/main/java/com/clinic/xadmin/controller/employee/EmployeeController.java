package com.clinic.xadmin.controller.employee;

import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = EmployeeControllerPath.BASE)
public class EmployeeController {

  private EmployeeService employeeService;

  @Autowired
  private EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping(value = EmployeeControllerPath.ROOT)
  public ResponseEntity<Employee> getEmployee() {
    return ResponseEntity.ofNullable(this.employeeService.getEmployee());
  }

}
