package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.controller.patient.PatientControllerSpecialValue;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.model.patient.SatuSehatPatientFilter;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
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

  @Autowired
  private PatientServiceImpl(SatuSehatAPICallWrapper apiCallWrapper) {
    this.apiCallWrapper = apiCallWrapper;
  }

  @Override
  public String getPatientFromSatuSehat(Clinic clinic, SatuSehatPatientFilter filter) {
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
          .gender(filter.getGender())
          .build();
    }
    ResponseEntity<StandardizedResourceResponse<PatientResourceResponse>>
        response = this.apiCallWrapper.wrapThrowableCall(endpoint, clinic.getCode());
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
