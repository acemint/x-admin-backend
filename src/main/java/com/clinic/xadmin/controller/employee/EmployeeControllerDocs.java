package com.clinic.xadmin.controller.employee;

import com.clinic.xadmin.controller.constant.CookieName;

public class EmployeeControllerDocs {

  public static final String LOGIN_SUMMARY = "Logging in to the current application";
  public static final String LOGIN_DESCRIPTION = "Returns a JWT Token to the HTTP header with a key of " + CookieName.CREDENTIAL + "."
      + "The returned JWT Token will be stored in cookie in favor of local storage / memory. This implies that Front-End won't need to handle the authenticated user, rather just use token synchronization for CSRF protection.<br><br>"
      + "Refer here for more reading: <br>"
      + "https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html<br>"
      + "https://security.stackexchange.com/questions/166724/should-i-use-csrf-protection-on-rest-api-endpoints/166798#166798.<br><br>"
      + "When a given JWT Token expires, the API which requires authentication will return 403 error (Forbidden) and therefore requires re-authentication, which means it is the responsibility of the Front-End to redirect user to /login page";

  public static final String REGISTER_SUMMARY = "Registers an employee within its' own clinic";
  public static final String REGISTER_DESCRIPTION = "The user of this API must have at least the role of ADMIN";

  public static final String GET_SELF_SUMMARY = "Get the data of current logged-in employee";
  public static final String GET_EMPLOYEES_SUMMARY = "Get the list of employees in own's clinic";
  public static final String GET_EMPLOYEES_DESCRIPTION = "Returns list of employees in the clinic that the authenticated user is at";

}
