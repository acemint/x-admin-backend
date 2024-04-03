package com.clinic.xadmin.repository.employee;


import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import org.springframework.data.domain.Page;

public interface EmployeeCustomRepository {

  Employee findEmployeeByEmailAddress(String clinicId, String emailAddress);
  Page<Employee> findByFilter(EmployeeFilter filter);

}
