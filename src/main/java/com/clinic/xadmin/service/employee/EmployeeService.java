package com.clinic.xadmin.service.employee;

import com.clinic.xadmin.controller.security.dto.request.RegisterEmployeeRequest;
import com.clinic.xadmin.entity.Employee;

public interface EmployeeService {

  Employee createEmployee(RegisterEmployeeRequest request);

}
