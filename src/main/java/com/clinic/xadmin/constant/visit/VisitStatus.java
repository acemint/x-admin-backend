package com.clinic.xadmin.constant.visit;

public enum VisitStatus {

  PLANNED("PLANNED", "planned"),
  CANCELLED("CANCELLED", "cancelled"),
  IN_PROGRESS("IN_PROGRESS", "in-progress"),
  COMPLETED("FINISHED", "completed"),
  DISCHARGED("DISCHARGED", "discharged");

  private final String backendValue;
  private final String satuSehatValue;

  VisitStatus(String backendValue, String satuSehatValue) {
    this.backendValue = backendValue;
    this.satuSehatValue = satuSehatValue;
  }

  public String getBackendValue() {
    return this.backendValue;
  }

  public String getSatuSehatValue() {
    return this.satuSehatValue;
  }
}
