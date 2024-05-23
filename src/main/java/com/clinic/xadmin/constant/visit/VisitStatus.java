package com.clinic.xadmin.constant.visit;

public enum VisitStatus {

  NOT_STARTED("NOT_STARTED", "Not Started"),
  CANCELLED("CANCELLED", "Cancelled"),
  IN_PROGRESS("IN_PROGRESS", "In Progress"),
  FINISHED("FINISHED", "Finished"),
  DISCHARGE_DISPOSITION("DISCHARGE_DISPOSITION", "Discharge Disposition");

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
