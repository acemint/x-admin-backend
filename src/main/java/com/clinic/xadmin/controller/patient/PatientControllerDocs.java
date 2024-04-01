package com.clinic.xadmin.controller.patient;

public class PatientControllerDocs {

  public static final String REGISTER_SUMMARY = "Registers a patient within its' own clinic";
  public static final String REGISTER_DESCRIPTION = "Creates a new patient within in the clinic that the current authenticated user is at, "
      + "so that they can be used to make visit.<br>"
      + "The user of this API must have at least the role of ADMIN";

  public static final String GET_PATIENTS_SUMMARY = "Get the list of patients in its' own clinic";
  public static final String GET_PATIENTS_DESCRIPTION = "Returns list of patients in the clinic that the current authenticated user is at."
      + "The user of this API must at least be ADMIN";

}
