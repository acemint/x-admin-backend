package com.clinic.xadmin.service.clinic;

import com.clinic.xadmin.dto.request.clinic.RegisterClinicRequest;
import com.clinic.xadmin.dto.request.clinic.UpdateClinicRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.model.clinic.ClinicFilter;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.clinic.ClinicSatuSehatCredentialRepository;
import com.satusehat.dto.response.oauth.OAuthResponse;
import com.satusehat.endpoint.oauth.SatuSehatOauthEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@Transactional
public class ClinicServiceImpl implements ClinicService {

  private final ClinicRepository clinicRepository;
  private final ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository;
  private final SatuSehatAPICallWrapper apiCallWrapper;

  @Autowired
  public ClinicServiceImpl(ClinicRepository clinicRepository, ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository, SatuSehatAPICallWrapper apiCallWrapper) {
    this.clinicRepository = clinicRepository;
    this.clinicSatuSehatCredentialRepository = clinicSatuSehatCredentialRepository;
    this.apiCallWrapper = apiCallWrapper;
  }

  @Override
  public Clinic createClinic(RegisterClinicRequest request) {
    Clinic existedClinic = this.clinicRepository.searchByName(request.getName());
    if (Objects.nonNull(existedClinic)) {
      throw new XAdminBadRequestException("Clinic name has existed: " + request.getName());
    }
    Clinic newClinic = Clinic.builder()
        .satuSehatClinicReferenceId(request.getSatuSehatOrganizationKey())
        .name(request.getName())
        .code(this.clinicRepository.getNextCode())
        .commissionFee(request.getCommissionFee())
        .sittingFee(request.getSittingFee())
        .medicalItemFee(request.getMedicalItemFee())
        .subscriptionTier(request.getSubscriptionTier())
        .subscriptionValidFrom(request.getSubscriptionValidFrom())
        .subscriptionValidTo(request.getSubscriptionValidTo())
        .build();
    this.clinicRepository.save(newClinic);

    ClinicSatuSehatCredential clinicSatuSehatCredential = ClinicSatuSehatCredential
        .builder()
        .clinicCode(newClinic.getCode())
        .satuSehatClientKey(request.getSatuSehatClientKey())
        .satuSehatOrganizationKey(request.getSatuSehatOrganizationKey())
        .satuSehatSecretKey(request.getSatuSehatSecretKey())
        .build();
    clinicSatuSehatCredential.setSatuSehatToken(this.fetchAccessToken(clinicSatuSehatCredential));
    this.clinicSatuSehatCredentialRepository.save(clinicSatuSehatCredential);
    return newClinic;
  }

  @Override
  public Clinic editClinic(String clinicCode, UpdateClinicRequest request) {
    Clinic existedClinic = this.clinicRepository.searchByCode(clinicCode);
    if (Objects.isNull(existedClinic)) {
      throw new XAdminBadRequestException("Clinic code does not exist: " + clinicCode);
    }
    existedClinic.setName(request.getName())
        .setSatuSehatClinicReferenceId(request.getSatuSehatOrganizationKey())
        .setCommissionFee(request.getCommissionFee())
        .setSittingFee(request.getSittingFee())
        .setMedicalItemFee(request.getMedicalItemFee())
        .setSubscriptionTier(request.getSubscriptionTier())
        .setSubscriptionValidFrom(request.getSubscriptionValidFrom())
        .setSubscriptionValidTo(request.getSubscriptionValidTo());
    this.clinicRepository.save(existedClinic);


    ClinicSatuSehatCredential clinicSatuSehatCredential = this.clinicSatuSehatCredentialRepository.findById(existedClinic.getCode())
        .orElse(ClinicSatuSehatCredential.builder().build());
    clinicSatuSehatCredential.setClinicCode(existedClinic.getCode())
        .setSatuSehatClientKey(request.getSatuSehatClientKey())
        .setSatuSehatOrganizationKey(request.getSatuSehatOrganizationKey())
        .setSatuSehatSecretKey(request.getSatuSehatSecretKey());
    clinicSatuSehatCredential.setSatuSehatToken(this.fetchAccessToken(clinicSatuSehatCredential));
    this.clinicSatuSehatCredentialRepository.save(clinicSatuSehatCredential);
    return existedClinic;
  }

  @Override
  public Clinic getClinic(String clinicCode) {
    Clinic existingClinic = this.clinicRepository.searchByCode(clinicCode);
    if (Objects.isNull(existingClinic)) {
      throw new XAdminBadRequestException("Clinic code does not exist: " + clinicCode);
    }
    return existingClinic;
  }

  @Override
  public Page<Clinic> getClinics(ClinicFilter clinicFilter) {
    Page<Clinic> clinics = this.clinicRepository.searchByFilter(clinicFilter);
    return clinics;
  }


  private String fetchAccessToken(ClinicSatuSehatCredential credential) {
    SatuSehatOauthEndpoint satuSehatOauthEndpoint = new SatuSehatOauthEndpoint(credential.getSatuSehatClientKey(), credential.getSatuSehatSecretKey());
    ResponseEntity<OAuthResponse> oAuthResponse = this.apiCallWrapper.call(satuSehatOauthEndpoint, credential);
    return oAuthResponse.getBody().getAccessToken();
  }

}
