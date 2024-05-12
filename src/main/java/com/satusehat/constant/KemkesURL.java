package com.satusehat.constant;

public interface KemkesURL {

  String BASE_PATH = "https://fhir.kemkes.go.id";

  interface Identity {

    String NIK = KemkesURL.BASE_PATH + "/id/nik";
    String MOTHER_NIK = KemkesURL.BASE_PATH + "/id/nik-ibu";

  }

  interface StructureDefinition {

    String PATIENT = "/r4/StructureDefinition/Patient";
    String ADMINISTRATIVE_CODE = "/r4/StructureDefinition/administrativeCode";

  }

}
