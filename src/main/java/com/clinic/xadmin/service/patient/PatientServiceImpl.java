package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.outbound.SatuSehatAPICallWrapper;
import com.clinic.xadmin.mapper.PatientMapper;
import com.clinic.xadmin.model.patient.PatientFilter;
import com.clinic.xadmin.model.patient.RegisterPatientData;
import com.clinic.xadmin.repository.clinic.ClinicRepository;
import com.clinic.xadmin.repository.patient.PatientRepository;
import com.satusehat.dto.response.StandardizedResourceResponse;
import com.satusehat.dto.response.patient.PatientResourceResponse;
import com.satusehat.endpoint.patient.SatuSehatSearchPatientByNIKEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PatientServiceImpl implements PatientService {

  private final PatientRepository patientRepository;
  private final ClinicRepository clinicRepository;
  private final SatuSehatAPICallWrapper apiCallWrapper;

  @Autowired
  private PatientServiceImpl(PatientRepository patientRepository, ClinicRepository clinicRepository, SatuSehatAPICallWrapper apiCallWrapper) {
    this.patientRepository = patientRepository;
    this.clinicRepository = clinicRepository;
    this.apiCallWrapper = apiCallWrapper;
  }

  @Override
  public Patient createPatient(RegisterPatientData registerPatientData) {
    Patient existingPatient = this.patientRepository.searchByClinicCodeAndEmailAddress(registerPatientData.getClinicCode(), registerPatientData.getEmailAddress());
    if (Objects.nonNull(existingPatient)) {
      throw new XAdminBadRequestException("this user has existed");
    }
    Clinic clinic = this.clinicRepository.searchByCode(registerPatientData.getClinicCode());

    Patient patient = PatientMapper.INSTANCE.createFrom(registerPatientData);

    patient.setCode(this.patientRepository.getNextCode());
    patient.setClinic(clinic);
    return this.patientRepository.save(patient);
  }

  @Override
  public Page<Patient> getPatients(PatientFilter patientFilter) {
    patientFilter.setClinicCode(patientFilter.getClinicCode());

    return this.patientRepository.searchByFilter(patientFilter);
  }

  @Override
  public Patient getPatient(Clinic clinic, String nik) {
    SatuSehatSearchPatientByNIKEndpoint endpoint = new SatuSehatSearchPatientByNIKEndpoint(nik);

    ResponseEntity<StandardizedResourceResponse<PatientResourceResponse>> response = this.apiCallWrapper.wrapThrowableCall(endpoint, clinic.getCode());
    if (response.getBody().getEntries().isEmpty()) {
      return null;
    }
    PatientResourceResponse patientResourceResponse = response.getBody().getEntries().getFirst().getResource();
    return Patient.builder()
        .code(patientResourceResponse.getId())
        .gender(patientResourceResponse.getGender())
        .build();
  }

}
