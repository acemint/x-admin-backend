package com.clinic.xadmin.exception.wrapper;

import com.clinic.xadmin.exception.XAdminAPICallException;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.exception.XAdminInternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.function.Supplier;

@Slf4j
public class APICallWrapper {

  public static <T> ResponseEntity<T> wrapThrowableCall(Supplier<ResponseEntity<T>> function) {
    try {
      ResponseEntity<T> result = function.get();
      if (result.getStatusCode().is4xxClientError()) {
        throw new XAdminBadRequestException(result.getBody().toString());
      } else if (result.getStatusCode().is5xxServerError()) {
        throw new XAdminInternalException(result.getBody().toString());
      }
      return result;
    } catch (HttpClientErrorException e) {
      throw new XAdminAPICallException(e);
    }
  }

}
