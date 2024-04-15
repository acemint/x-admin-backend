package com.clinic.xadmin.controller.member;

public interface MemberControllerDocs {


  String REGISTER_MANAGER_REQUEST = "Registers a member with role Manager within its' own clinic";
  String REGISTER_MANAGER_DESCRIPTION = "Role Allowed: ROLE_CLINIC_ADMIN";

  String REGISTER_PRACTITIONER_SUMMARY = "Registers a member with role Practitioner within its' own clinic";
  String REGISTER_PRACTITIONER_DESCRIPTION = "Will access SatuSehat to check if the patient data has existed and obtain its' IHS number (e.g.: P1203102)"
      + "Role Allowed: ROLE_CLINIC_ADMIN";


  String REGISTER_PATIENT_SUMMARY = "Registers a member with role Patient within its' own clinic";
  String REGISTER_PATIENT_DESCRIPTION = "Will access SatuSehat to check if the patient data has existed and obtain its' IHS number (e.g.: P1203102)"
      + "Role Allowed: ROLE_CLINIC_ADMIN";

  String GET_SELF_SUMMARY = "Get the data of current logged-in member";
  String GET_MEMBER_SUMMARY = "Get the list of member in own's clinic";
  String GET_MEMBER_DESCRIPTION = "Returns list of member in the clinic that the authenticated user is at."
      + "The user of this API must at least be ADMIN.<br><br>"
      + "Request Parameters: <br>"
      + "`name`: to filter by \"firstName\" or \"lastName\"<br>"
      + "`sortBy`: to sort the result in a certain way, support multiple sort fields<br>"
      + "`sortDirection`: to sort the result in a given direction (1 sort direction will apply to all sort by fields)<br>"
      + "`pageNumber`: returns the pageNumber, the index starts from 0<br>"
      + "`pageSize`: the size of data requested per page<br><br>"
      + "Role Allowed: All Authenticated User";

}
