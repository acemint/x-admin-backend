package com.clinic.xadmin.service.employee;

import com.clinic.xadmin.dto.request.employee.RegisterEmployeeRequest;
import com.clinic.xadmin.dto.request.employee.ResetPasswordRequest;
import com.clinic.xadmin.entity.Employee;

public interface EmployeeService {

  Employee createEmployee(RegisterEmployeeRequest request);

}
