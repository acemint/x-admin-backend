package com.clinic.xadmin.controller.root;

public interface RootPublicControllerDocs {

  String LOGIN_SUMMARY = "Logging in to the current application";
  String LOGIN_DESCRIPTION = "Authenticates a user in the system by using session-id. See here for reference:<br>"
      + "https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html"
      + "https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html<br>"
      + "https://security.stackexchange.com/questions/166724/should-i-use-csrf-protection-on-rest-api-endpoints/166798#166798.<br><br>"
      + "When a given session expires, the API which requires authentication will return 403 error (Forbidden) and therefore requires re-authentication."
      + "This will be a responsibility of the Front-End to redirect user to /login page<br><br>"
      + "Role Allowed: Unauthenticated User";

  String LOGOUT_SUMMARY = "Logs out of current application";
  String LOGOUT_DESCRIPTION = "Logs out of a user from the system";

}
