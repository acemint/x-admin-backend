package com.clinic.xadmin.exception;

public class XAdminIllegalStateException extends IllegalStateException {

  public XAdminIllegalStateException() {
    super("This case should not happened");
  }

  public XAdminIllegalStateException(String message) {
    super(message);
  }

}
