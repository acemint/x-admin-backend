package com.clinic.xadmin.repository.employee;

import com.clinic.xadmin.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String>, EmployeeCustomRepository {

}
