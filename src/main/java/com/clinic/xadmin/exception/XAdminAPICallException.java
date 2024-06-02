package com.clinic.xadmin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@Getter
public class XAdminAPICallException extends RuntimeException {

  private HttpStatusCode responseStatus;

  public XAdminAPICallException(HttpStatusCodeException e) {
    super(e);
  }

  public XAdminAPICallException(HttpStatusCode httpStatusCode, String message) {
    super(message);
    this.responseStatus = httpStatusCode;
  }

}
