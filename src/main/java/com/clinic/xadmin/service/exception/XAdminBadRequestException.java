package com.clinic.xadmin.service.exception;

public class XAdminBadRequestException extends RuntimeException {

  public XAdminBadRequestException() {
    super("There is something wrong with the request");
  }

  public XAdminBadRequestException(String message) {
    super(message);
  }

}
