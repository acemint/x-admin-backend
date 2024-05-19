package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.controller.patient.PatientControllerSpecialValue;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.mapper.MemberMapper;
import com.clinic.xadmin.model.patient.SatuSehatPatientFilter;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.repository.clinic.ClinicSatuSehatCredentialRepository;
import com.satusehat.dto.request.patient.SatuSehatCreatePatientRequest;
import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.dto.response.patient.PatientCreationResourceResponse;
import com.satusehat.dto.response.patient.PatientSearchResourceResponse;
import com.satusehat.endpoint.SatuSehatEndpoint;
import com.satusehat.endpoint.patient.SatuSehatRegisterPatientByNIKEndpoint;
import com.satusehat.endpoint.patient.SatuSehatSearchPatientByDescriptionEndpoint;
import com.satusehat.endpoint.patient.SatuSehatSearchPatientByMothersNIKEndpoint;
import com.satusehat.endpoint.patient.SatuSehatSearchPatientByNIKEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
    SatuSehatEndpoint<StandardizedResourceResponse<PatientSearchResourceResponse>> endpoint = null;
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
    ResponseEntity<StandardizedResourceResponse<PatientSearchResourceResponse>>
        response = this.apiCallWrapper.call(endpoint, clinicSatuSehatCredentialRepository.searchMainClinicApp());
    if (response.getBody().getEntries().isEmpty()) {
      return null;
    }
    if (response.getBody().getTotal() > 1) {
      throw new XAdminBadRequestException("Please specify the field search if you SearchByDescription, use full name!");
    }
    PatientSearchResourceResponse
        patientSearchResourceResponse = response.getBody().getEntries().getFirst().getResource();
    return patientSearchResourceResponse.getId();
  }

  @Override
  public String getOrCreateSatuSehatPatient(Member member) {
    if (StringUtils.hasText(member.getSatuSehatPatientReferenceId())) {
      return member.getSatuSehatPatientReferenceId();
    }

    String ihsCode = this.callGETPatientByAllEndpoints(member);
    if (StringUtils.hasText(ihsCode)) {
      return ihsCode;
    }

    ihsCode = callPOSTCreatePatient(member, member.getClinic());
    if (StringUtils.hasText(ihsCode)) {
      return ihsCode;
    }
    throw new XAdminBadRequestException("Unable to get or create data");
  }

  private String callGETPatientByAllEndpoints(Member member) {
    // Try to obtain IHS Code by all provided endpoints
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    if (Objects.nonNull(member.getNik())) {
      String ihsCode = callGETPatientEndpoint(SatuSehatSearchPatientByNIKEndpoint.builder()
          .nik(member.getNik())
          .build());
      if (Objects.nonNull(ihsCode)) {
        return ihsCode;
      }
    }

    if (Objects.nonNull(member.getMotherNik())) {
      String ihsCode = callGETPatientEndpoint(SatuSehatSearchPatientByMothersNIKEndpoint.builder()
          .nik(member.getMotherNik())
          .build());
      if (Objects.nonNull(ihsCode)) {
        return ihsCode;
      }
    }

    if (Objects.nonNull(member.getFirstName()) && Objects.nonNull(member.getGender()) && Objects.nonNull(member.getDateOfBirth())) {
      String ihsCode = callGETPatientEndpoint(SatuSehatSearchPatientByDescriptionEndpoint.builder()
          .name(member.getFirstName() + member.getLastName())
          .birthDate(member.getDateOfBirth().format(formatter))
          .gender(member.getGender().toLowerCase())
          .build());
      if (Objects.nonNull(ihsCode)) {
        return ihsCode;
      }
    }
    return null;
  }

  private String callGETPatientEndpoint(SatuSehatEndpoint<StandardizedResourceResponse<PatientSearchResourceResponse>> endpoint) throws HttpStatusCodeException {
    ResponseEntity<StandardizedResourceResponse<PatientSearchResourceResponse>>
        response = this.apiCallWrapper.call(endpoint, clinicSatuSehatCredentialRepository.searchMainClinicApp());
    if (response.getBody().getEntries().isEmpty()) {
      return null;
    }
    if (response.getBody().getTotal() > 1) {
      return null;
    }
    PatientSearchResourceResponse
        patientSearchResourceResponse = response.getBody().getEntries().getFirst().getResource();
    return patientSearchResourceResponse.getId();
  }

  private String callPOSTCreatePatient(Member member, Clinic clinic) throws HttpStatusCodeException {
    SatuSehatCreatePatientRequest satuSehatCreatePatientRequest = MemberMapper.INSTANCE.convertToSatuSehatAPIRequest(member);
    SatuSehatRegisterPatientByNIKEndpoint endpoint = SatuSehatRegisterPatientByNIKEndpoint.builder()
        .satuSehatCreatePatientRequest(satuSehatCreatePatientRequest)
        .build();
    ResponseEntity<PatientCreationResourceResponse> response = this.apiCallWrapper.call(endpoint, clinic.getCode());
    return response.getBody().getContent().getPatientId();
  }


}
