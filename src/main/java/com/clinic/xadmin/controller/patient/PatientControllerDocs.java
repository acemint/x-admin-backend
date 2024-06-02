package com.clinic.xadmin.controller.patient;

public interface PatientControllerDocs {

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

  String SEARCH_PATIENT_SUMMARY = "[`DEPRECATED`] Get patient IHS Code from Satu Sehat";
  String SEARCH_PATIENT_DESCRIPTION = "Available searchBy are: " + "\"nik\", \"mother-nik\", \"description\", " + "<br>"
      + "Conditions: <br>"
      + "when searching by NIK / mother's NIK, will only return 1<br>"
      + "when searching by description, please specify the full name<br><br>"
      + "`nik`: Search by NIK<br>"
      + "`mother-nik`: Search by mother's NIK<br>"
      + "`name`: Used when searching by is \"description\". Search by full name of the person<br>"
      + "`date-of-birth`: Used when searching by is \"description\". Specify the Date of Birth of the person, in YYYY-MM-DD<br>"
      + "`gender`: Used when searching by \"description\". Gender can only be male or female<br><br>"
      + "Role Allowed: All Authenticated User";

}
