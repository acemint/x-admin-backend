package com.clinic.xadmin.repository.employee;


import com.clinic.xadmin.entity.Employee;

public interface EmployeeCustomRepository {

  Employee findEmployeeByEmailAddress(String emailAddress);

}
