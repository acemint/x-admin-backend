package com.clinic.xadmin.controller.employee;

public interface EmployeeControllerDocs {

  String REGISTER_SUMMARY = "Registers an employee within its' own clinic";
  String REGISTER_DESCRIPTION = "Role Allowed: ROLE_CLINIC_ADMIN";

  String GET_SELF_SUMMARY = "Get the data of current logged-in employee";
  String GET_EMPLOYEES_SUMMARY = "Get the list of employees in own's clinic";
  String GET_EMPLOYEES_DESCRIPTION = "Returns list of employees in the clinic that the authenticated user is at."
      + "The user of this API must at least be ADMIN.<br><br>"
      + "Request Parameters: <br>"
      + "`name`: to filter by \"firstName\" or \"lastName\"<br>"
      + "`sortBy`: to sort the result in a certain way, support multiple sort fields<br>"
      + "`sortDirection`: to sort the result in a given direction (1 sort direction will apply to all sort by fields)<br>"
      + "`pageNumber`: returns the pageNumber, the index starts from 0<br>"
      + "`pageSize`: the size of data requested per page<br><br>"
      + "Role Allowed: All Authenticated User";

}
