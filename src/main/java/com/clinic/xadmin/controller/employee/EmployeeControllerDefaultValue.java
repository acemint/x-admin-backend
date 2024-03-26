package com.clinic.xadmin.controller.employee;

import com.clinic.xadmin.repository.employee.EmployeeCustomRepositoryImpl;

import java.util.Set;


public interface EmployeeControllerDefaultValue {
  String DEFAULT_SORT_ORDER = "ASC";
  String DEFAULT_PAGE_NUMBER = "0";
  String DEFAULT_PAGE_SIZE = "10";
  Set<String> AVAILABLE_SORTED_BY = EmployeeCustomRepositoryImpl.AVAILABLE_SORTED_BY;

}
