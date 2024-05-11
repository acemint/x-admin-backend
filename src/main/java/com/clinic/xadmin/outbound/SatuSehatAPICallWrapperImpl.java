package com.clinic.xadmin.outbound;

import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import com.clinic.xadmin.exception.XAdminAPICallException;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.exception.XAdminInternalException;
import com.clinic.xadmin.repository.clinic.ClinicSatuSehatCredentialRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satusehat.dto.response.StandardizedErrorResourceResponse;
import com.satusehat.dto.response.oauth.OAuthResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.endpoint.oauth.SatuSehatOauthEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;
import java.util.stream.Collectors;

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
    try {
      return baseEndpoint.setAuthToken(credential.getSatuSehatToken()).getMethodCall();
    }
    catch (HttpClientErrorException e) {
      return this.handle4xxClientException(baseEndpoint, credential, e);
    }
    catch (HttpServerErrorException e) {
      return this.handle5xxClientException(e);
    }
  }

  @Override
  public <T> ResponseEntity<T> call(SatuSehatEndpoint<T> baseEndpoint, String clinicCode) {
    Optional<ClinicSatuSehatCredential> credential = this.credentialRepository.findById(clinicCode);
    if (credential.isEmpty()) {
      throw new XAdminBadRequestException("clinic not found");
    }
    return this.call(baseEndpoint, credential.get());
  }

  private <T> ResponseEntity<T> handle4xxClientException(SatuSehatEndpoint<T> baseEndpoint, ClinicSatuSehatCredential credential, HttpClientErrorException exception) {
    if (exception.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED) && !(baseEndpoint instanceof SatuSehatOauthEndpoint)) {
     this.refetchSatuSehatAccessToken(credential);
     return baseEndpoint.setAuthToken(credential.getSatuSehatToken()).getMethodCall();
    }
    throw new XAdminAPICallException(exception.getStatusCode(), this.getMessage(exception));
  }

  private <T> ResponseEntity<T> handle5xxClientException(HttpServerErrorException exception) {
    return ResponseEntity.internalServerError().build();
  }

  private void refetchSatuSehatAccessToken(ClinicSatuSehatCredential credential) {
    SatuSehatOauthEndpoint satuSehatOauthEndpoint = new SatuSehatOauthEndpoint(credential.getSatuSehatClientKey(), credential.getSatuSehatSecretKey());
    ResponseEntity<OAuthResponse> oauthEndpointResponse = satuSehatOauthEndpoint.getMethodCall();
    // TODO: If get 429 error too many request, create a timer to retry within 1 minute
    try {
      OAuthResponse oAuthResponse = oauthEndpointResponse.getBody();
      credential.setSatuSehatToken(oAuthResponse.getAccessToken());
      this.credentialRepository.save(credential);
    }
    catch (HttpClientErrorException e) {
      throw new XAdminInternalException("Unable to refetch token, please check log");
    }
  }

  private String getMessage(HttpClientErrorException e) {
    try {
      StandardizedErrorResourceResponse error = objectMapper.readValue(e.getResponseBodyAsString(), new TypeReference<>() {});
      return error.getIssue().stream()
          .map(i -> i.getDetails().getText()).collect(Collectors.joining("\n"));
    } catch (Exception exception) {
      return e.getResponseBodyAsString();
    }
  }


}
