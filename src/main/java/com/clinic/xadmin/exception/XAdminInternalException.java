package com.clinic.xadmin.exception;

public class XAdminInternalException extends RuntimeException {

  public XAdminInternalException() {
    super("Admin application went wrong");
  }

  public XAdminInternalException(String message) {
    super(message);
  }

}
