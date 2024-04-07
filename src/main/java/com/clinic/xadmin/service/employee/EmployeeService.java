package com.clinic.xadmin.service.employee;

import com.clinic.xadmin.dto.request.employee.ResetPasswordRequest;
import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import com.clinic.xadmin.model.employee.RegisterEmployeeData;
import org.springframework.data.domain.Page;

public interface EmployeeService {

  Employee createEmployee(RegisterEmployeeData registerEmployeeData);
  Page<Employee> getEmployees(EmployeeFilter employeeFilter);

  Employee resetPassword(ResetPasswordRequest request);

}
