package com.clinic.xadmin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public class XAdminAPICallException extends RuntimeException {

  private HttpStatusCode responseStatus;
  private String message;

  public XAdminAPICallException(HttpClientErrorException e) {
    super(e);
    this.responseStatus = e.getStatusCode();
    this.message = e.getMessage();
  }

  public XAdminAPICallException(String message) {
    super(message);
  }

}
