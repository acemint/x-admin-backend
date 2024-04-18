package com.clinic.xadmin.outbound;

import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import com.satusehat.endpoint.SatuSehatEndpoint;
import org.springframework.http.ResponseEntity;


public interface SatuSehatAPICallWrapper {

  // automatically retry to authenticate when a given method call failed
  <T> ResponseEntity<T> wrapThrowableCall(SatuSehatEndpoint<T> baseEndpoint, ClinicSatuSehatCredential credential);
  <T> ResponseEntity<T> wrapThrowableCall(SatuSehatEndpoint<T> baseEndpoint, String clinicCode);

}
