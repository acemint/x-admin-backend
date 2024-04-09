package com.clinic.xadmin.service.clinic;

import com.clinic.xadmin.dto.request.clinic.RegisterClinicRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.repository.ClinicSatuSehatCredentialRepository;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ClinicServiceImpl implements ClinicService {

  private final ClinicRepository clinicRepository;
  private final ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository;

  @Autowired
  private ClinicServiceImpl(ClinicRepository clinicRepository, ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository) {
    this.clinicRepository = clinicRepository;
    this.clinicSatuSehatCredentialRepository = clinicSatuSehatCredentialRepository;
  }

  @Override
  public Clinic createClinic(RegisterClinicRequest request) {
    Clinic existedClinic = this.clinicRepository.findByName(request.getName());
    if (Objects.nonNull(existedClinic)) {
      throw new XAdminBadRequestException("Clinic name has existed: " + request.getName());
    }
    Clinic newClinic = Clinic.builder()
        .name(request.getName())
        .code(this.clinicRepository.getNextCode())
        .commissionFee(request.getCommissionFee())
        .sittingFee(request.getSittingFee())
        .medicalItemFee(request.getMedicalItemFee())
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
}
