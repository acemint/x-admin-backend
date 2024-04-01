package com.clinic.xadmin.controller.employee;

public interface EmployeeControllerDocs {

  String LOGIN_SUMMARY = "Logging in to the current application";
  String LOGIN_DESCRIPTION = "Authenticates a user in the system by using session-id. See here for reference:<br>"
      + "https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html"
      + "https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html<br>"
      + "https://security.stackexchange.com/questions/166724/should-i-use-csrf-protection-on-rest-api-endpoints/166798#166798.<br><br>"
      + "When a given session expires, the API which requires authentication will return 403 error (Forbidden) and therefore requires re-authentication."
      + "This will be a responsibility of the Front-End to redirect user to /login page";

  String REGISTER_SUMMARY = "Registers an employee within its' own clinic";
  String REGISTER_DESCRIPTION = "The user of this API must have at least the role of ADMIN";

  String GET_SELF_SUMMARY = "Get the data of current logged-in employee";
  String GET_EMPLOYEES_SUMMARY = "Get the list of employees in own's clinic";
  String GET_EMPLOYEES_DESCRIPTION = "Returns list of employees in the clinic that the authenticated user is at";

}
