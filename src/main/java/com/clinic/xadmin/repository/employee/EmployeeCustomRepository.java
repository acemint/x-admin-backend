package com.clinic.xadmin.repository.employee;


import com.clinic.xadmin.entity.Employee;
import com.clinic.xadmin.model.employee.EmployeeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface EmployeeCustomRepository {

  Employee findEmployeeByUsername(String username);
  Employee findEmployeeByClinicIdAndEmailAddress(String clinicId, String emailAddress);
  Page<Employee> findByFilter(EmployeeFilter filter);

}
