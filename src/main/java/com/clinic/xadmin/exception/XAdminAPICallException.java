package com.clinic.xadmin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public class XAdminAPICallException extends RuntimeException {

  private final HttpStatusCode responseStatus;

  public XAdminAPICallException(HttpClientErrorException e) {
    super(e.getMessage());
    this.responseStatus = e.getStatusCode();
  }

  public XAdminAPICallException(HttpStatusCode httpStatusCode, String message) {
    super(message);
    this.responseStatus = httpStatusCode;
  }

}
