package com.clinic.xadmin.controller.patient;

public interface PatientControllerDocs {

  String REGISTER_SUMMARY = "Registers a patient within its' own clinic";
  String REGISTER_DESCRIPTION = "Creates a new patient within in the clinic that the current authenticated user is at, "
      + "so that they can be used to make visit.<br><br>"
      + "Role Allowed: ROLE_CLINIC_ADMIN";

  String GET_PATIENTS_SUMMARY = "Get the list of patients in its' own clinic";
  String GET_PATIENTS_DESCRIPTION = "Returns list of patients in the clinic that the current authenticated user is at."
      + "The user of this API must at least be ADMIN.<br><br>"
      + "Request Parameters: <br>"
      + "`name`: to filter by \"firstName\" or \"lastName\"<br>"
      + "`sortBy`: to sort the result in a certain way, support multiple sort fields<br>"
      + "`sortDirection`: to sort the result in a given direction (1 sort direction will apply to all sort by fields)<br>"
      + "`pageNumber`: returns the pageNumber, the index starts from 0<br>"
      + "`pageSize`: the size of data requested per page<br><br>"
      + "Role Allowed: All Authenticated User";

  String SEARCH_PATIENTS_SUMMARY = "Get the list of patients from Satu Sehat";
  String SEARCH_PATIENTS_DESCRIPTION = "Role Allowed: All Authenticated User";

}
