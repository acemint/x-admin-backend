package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.controller.patient.PatientControllerSpecialValue;
import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.model.patient.SatuSehatPatientFilter;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.repository.clinic.ClinicSatuSehatCredentialRepository;
import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.dto.response.patient.PatientResourceResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.endpoint.patient.SatuSehatSearchPatientByDescriptionEndpoint;
import com.satusehat.endpoint.patient.SatuSehatSearchPatientByMothersNIKEndpoint;
import com.satusehat.endpoint.patient.SatuSehatSearchPatientByNIKEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

  private final SatuSehatAPICallWrapper apiCallWrapper;
  private final ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository;

  @Autowired
  private PatientServiceImpl(SatuSehatAPICallWrapper apiCallWrapper, ClinicSatuSehatCredentialRepository clinicSatuSehatCredentialRepository) {
    this.apiCallWrapper = apiCallWrapper;
    this.clinicSatuSehatCredentialRepository = clinicSatuSehatCredentialRepository;
  }

  @Override
  public String getPatientFromSatuSehat(SatuSehatPatientFilter filter) {
    SatuSehatEndpoint<StandardizedResourceResponse<PatientResourceResponse>> endpoint = null;
    if (filter.getSearchBy().equals(PatientControllerSpecialValue.SEARCH_BY_NIK)) {
      endpoint = SatuSehatSearchPatientByNIKEndpoint.builder()
          .nik(filter.getNik())
          .build();
    }
    else if (filter.getSearchBy().equals(PatientControllerSpecialValue.SEARCH_BY_MOTHER_NIK)) {
      endpoint = SatuSehatSearchPatientByMothersNIKEndpoint.builder()
          .nik(filter.getMotherNik())
          .build();
    }
    else if (filter.getSearchBy().equals(PatientControllerSpecialValue.SEARCH_BY_DESCRIPTION)) {
      endpoint = SatuSehatSearchPatientByDescriptionEndpoint.builder()
          .name(filter.getName())
          .birthDate(filter.getDateOfBirth())
          .gender(filter.getGender().toLowerCase())
          .build();
    }
    ResponseEntity<StandardizedResourceResponse<PatientResourceResponse>>
        response = this.apiCallWrapper.wrapThrowableCall(endpoint, clinicSatuSehatCredentialRepository.searchMainClinicApp());
    if (response.getBody().getEntries().isEmpty()) {
      return null;
    }
    if (response.getBody().getTotal() > 1) {
      throw new XAdminBadRequestException("Please specify the field search if you SearchByDescription, use full name!");
    }
    PatientResourceResponse patientResourceResponse = response.getBody().getEntries().getFirst().getResource();
    return patientResourceResponse.getId();
  }

}
