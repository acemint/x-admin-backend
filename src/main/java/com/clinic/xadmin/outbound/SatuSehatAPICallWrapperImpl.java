package com.clinic.xadmin.outbound;

import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import com.clinic.xadmin.exception.XAdminAPICallException;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.exception.XAdminIllegalStateException;
import com.clinic.xadmin.exception.XAdminInternalException;
import com.clinic.xadmin.repository.clinic.ClinicSatuSehatCredentialRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satusehat.dto.response.oauth.OAuthResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.endpoint.oauth.SatuSehatOauthEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;
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
  public <T> ResponseEntity<T> wrapThrowableCall(SatuSehatEndpoint<T> baseEndpoint, ClinicSatuSehatCredential credential) {
    try {
      return baseEndpoint.setAuthToken(credential.getSatuSehatToken()).getMethodCall();
    }
    catch (HttpClientErrorException e) {
      return this.automaticRetryOnExpiredToken(baseEndpoint, credential, e);

    }
  }

  @Override
  public <T> ResponseEntity<T> wrapThrowableCall(SatuSehatEndpoint<T> baseEndpoint, String clinicCode) {
    Optional<ClinicSatuSehatCredential> credential = this.credentialRepository.findById(clinicCode);
    if (credential.isEmpty()) {
      throw new XAdminBadRequestException("clinic not found");
    }
    return this.wrapThrowableCall(baseEndpoint, credential.get());
  }

  private <T> ResponseEntity<T> automaticRetryOnExpiredToken(SatuSehatEndpoint<T> baseEndpoint, ClinicSatuSehatCredential credential, HttpClientErrorException exception) {
    if (exception.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED) && !(baseEndpoint instanceof SatuSehatOauthEndpoint)) {
     this.refetchSatuSehatAccessToken(credential);
     return baseEndpoint.setAuthToken(credential.getSatuSehatToken()).getMethodCall();
    }
    else if (exception.getStatusCode().is4xxClientError()) {
      throw new XAdminBadRequestException(exception.getResponseBodyAsString().replaceAll("\n", ""));
    }
    else if (exception.getStatusCode().is5xxServerError()) {
      throw new XAdminInternalException(exception.getResponseBodyAsString().replaceAll("\n", ""));
    }
    throw new XAdminIllegalStateException(exception.getResponseBodyAsString().replaceAll("\n", ""));
  }

  private void refetchSatuSehatAccessToken(ClinicSatuSehatCredential credential) {
    SatuSehatOauthEndpoint satuSehatOauthEndpoint = new SatuSehatOauthEndpoint(credential.getSatuSehatClientKey(), credential.getSatuSehatSecretKey());
    ResponseEntity<OAuthResponse> oauthEndpointResponse = satuSehatOauthEndpoint.getMethodCall();
    // TODO: If get 429 error too many request, create a timer to retry within 1 minute
    if (oauthEndpointResponse.getStatusCode().is2xxSuccessful()) {
      OAuthResponse oAuthResponse = oauthEndpointResponse.getBody();
      credential.setSatuSehatToken(oAuthResponse.getAccessToken());
      this.credentialRepository.save(credential);
    }
    else {
      throw new XAdminInternalException("Unable to refetch token, please check log");
    }
  }


}