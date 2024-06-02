package com.clinic.xadmin.controller.clinic;

public interface ClinicControllerDocs {

  String REGISTER_SUMMARY = "Creates a clinic";
  String REGISTER_DESCRIPTION = "Role allowed: ROLE_DEVELOPER";

  String EDIT_SUMMARY = "Updates an existing clinic";
  String EDIT_DESCRIPTION = "Role allowed: ROLE_DEVELOPER";

  String GET_CLINIC_SUMMARY = "Get a clinic information";
  String GET_CLINIC_DESCRIPTION = "Role allowed: ROLE_CLINIC_ADMIN";

  String GET_LIST_CLINIC_SUMMARY = "Get the list of clinic information";
  String GET_LIST_CLINIC_DESCRIPTION = "Role allowed: ROLE_DEVELOPER";

}
