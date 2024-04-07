package com.clinic.xadmin.repository.employee;


import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import org.springframework.data.domain.Page;

public interface EmployeeCustomRepository {

  Employee searchByUsername(String username);
  Employee searchByClinicCodeAndEmailAddress(String clinicCode, String emailAddress);
  Page<Employee> searchByFilter(EmployeeFilter filter);

}
