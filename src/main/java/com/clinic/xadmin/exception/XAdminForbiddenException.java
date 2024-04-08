package com.clinic.xadmin.exception;

public class XAdminForbiddenException extends RuntimeException {

  public XAdminForbiddenException() {
    super("Forbidden access");
  }

  public XAdminForbiddenException(String message) {
    super(message);
  }

}
