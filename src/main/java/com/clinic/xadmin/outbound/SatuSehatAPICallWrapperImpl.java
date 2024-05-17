package com.clinic.xadmin.outbound;

import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import com.clinic.xadmin.exception.XAdminIllegalStateException;
import com.clinic.xadmin.repository.clinic.ClinicSatuSehatCredentialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satusehat.dto.response.oauth.OAuthResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.endpoint.oauth.SatuSehatOauthEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;

@Component
@Slf4j
public class SatuSehatAPICallWrapperImpl implements SatuSehatAPICallWrapper {

  private final ClinicSatuSehatCredentialRepository credentialRepository;
  private final ObjectMapper objectMapper;

  @Autowired
  public SatuSehatAPICallWrapperImpl(ClinicSatuSehatCredentialRepository credentialRepository, ObjectMapper objectMapper) {
    this.credentialRepository = credentialRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public <T> ResponseEntity<T> call(SatuSehatEndpoint<T> baseEndpoint, ClinicSatuSehatCredential credential) {
    return this.callMethodWithRetry(baseEndpoint, credential);
  }

  @Override
  public <T> ResponseEntity<T> call(SatuSehatEndpoint<T> baseEndpoint, String clinicCode) {
    Optional<ClinicSatuSehatCredential> credential = this.credentialRepository.findById(clinicCode);
    if (credential.isEmpty()) {
      throw new XAdminIllegalStateException("impossible state, clinic not found");
    }
    return this.callMethodWithRetry(baseEndpoint, credential.get());
  }

  private <T> ResponseEntity<T> callMethodWithRetry(SatuSehatEndpoint<T> baseEndpoint, ClinicSatuSehatCredential credential) {
    HttpStatusCodeException e = null;
    try {
      return baseEndpoint.setAuthToken(credential.getSatuSehatToken()).performHttpRequest();
    }
    catch (HttpStatusCodeException exception) {
      e = exception;
    }

    boolean isRetryable = e.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED) && !(baseEndpoint instanceof SatuSehatOauthEndpoint);
    if (!isRetryable) {
      throw e;
    }

    this.refetchSatuSehatAccessToken(credential);
    return baseEndpoint.setAuthToken(credential.getSatuSehatToken()).performHttpRequest();
  }

  private void refetchSatuSehatAccessToken(ClinicSatuSehatCredential credential) {
    SatuSehatOauthEndpoint satuSehatOauthEndpoint = new SatuSehatOauthEndpoint(credential.getSatuSehatClientKey(), credential.getSatuSehatSecretKey());
    try {
      ResponseEntity<OAuthResponse> oauthEndpointResponse = satuSehatOauthEndpoint.performHttpRequest();
      OAuthResponse oAuthResponse = oauthEndpointResponse.getBody();
      credential.setSatuSehatToken(oAuthResponse.getAccessToken());
      this.credentialRepository.save(credential);
    }
    // TODO: If get 429 error too many request, create a timer to retry within 1 minute
    catch (HttpStatusCodeException e) {
      throw new XAdminIllegalStateException("Unable to refetch token, please check log");
    }
  }


}
