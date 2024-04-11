package com.clinic.xadmin.repository.employee;

import com.clinic.xadmin.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, String>, EmployeeCustomRepository {

  @Query(value = "SELECT CONCAT('EMP-', nextval('employee_sequence'))", nativeQuery = true)
  String getNextCode();

}
