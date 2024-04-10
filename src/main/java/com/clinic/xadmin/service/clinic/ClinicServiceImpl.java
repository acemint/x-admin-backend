package com.clinic.xadmin.service.clinic;

import com.clinic.xadmin.dto.request.clinic.RegisterClinicRequest;
import com.clinic.xadmin.dto.request.clinic.UpdateClinicRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.clinic.ClinicSatuSehatCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@Transactional
public class ClinicServiceImpl implements ClinicService {

  private final ClinicRepository clinicRepository;
  private final ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository;

  @Autowired
  public ClinicServiceImpl(ClinicRepository clinicRepository, ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository) {
    this.clinicRepository = clinicRepository;
    this.clinicSatuSehatCredentialRepository = clinicSatuSehatCredentialRepository;
  }

  @Override
  public Clinic createClinic(RegisterClinicRequest request) {
    Clinic existedClinic = this.clinicRepository.searchByName(request.getName());
    if (Objects.nonNull(existedClinic)) {
      throw new XAdminBadRequestException("Clinic name has existed: " + request.getName());
    }
    Clinic newClinic = Clinic.builder()
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
        .clinicId(newClinic.getId())
        .satuSehatClientKey(request.getSatuSehatClientKey())
        .satuSehatOrganizationKey(request.getSatuSehatOrganizationKey())
        .satuSehatSecretKey(request.getSatuSehatSecretKey())
        .build();
    this.clinicSatuSehatCredentialRepository.save(clinicSatuSehatCredential);
    return newClinic;
  }

  @Override
  public Clinic editClinic(String clinicCode, UpdateClinicRequest request) {
    Clinic existedClinic = this.clinicRepository.searchByCode(clinicCode);
    if (Objects.isNull(existedClinic)) {
      throw new XAdminBadRequestException("Clinic code does not exist: " + clinicCode);
    }
    existedClinic.setName(request.getName());
    existedClinic.setCommissionFee(request.getCommissionFee());
    existedClinic.setSittingFee(request.getSittingFee());
    existedClinic.setMedicalItemFee(request.getMedicalItemFee());
    existedClinic.setSubscriptionTier(request.getSubscriptionTier());
    existedClinic.setSubscriptionValidFrom(request.getSubscriptionValidFrom());
    existedClinic.setSubscriptionValidTo(request.getSubscriptionValidTo());
    this.clinicRepository.save(existedClinic);


    ClinicSatuSehatCredential clinicSatuSehatCredential = this.clinicSatuSehatCredentialRepository.findById(existedClinic.getId())
        .orElse(ClinicSatuSehatCredential.builder().build());
    clinicSatuSehatCredential.setClinicId(existedClinic.getId());
    clinicSatuSehatCredential.setSatuSehatClientKey(request.getSatuSehatClientKey());
    clinicSatuSehatCredential.setSatuSehatOrganizationKey(request.getSatuSehatOrganizationKey());
    clinicSatuSehatCredential.setSatuSehatSecretKey(request.getSatuSehatSecretKey());
    this.clinicSatuSehatCredentialRepository.save(clinicSatuSehatCredential);
    return existedClinic;
  }


}
