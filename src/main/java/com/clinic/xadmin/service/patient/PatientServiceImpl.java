package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.dto.request.patient.RegisterPatientRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.exception.XAdminBadRequestException;
import com.clinic.xadmin.mapper.PatientMapper;
import com.clinic.xadmin.model.patient.PatientFilter;
import com.clinic.xadmin.repository.patient.PatientRepository;
import com.clinic.xadmin.service.helper.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PatientServiceImpl implements PatientService {

  private final PatientRepository patientRepository;
  private final ServiceHelper serviceHelper;

  @Autowired
  private PatientServiceImpl(PatientRepository patientRepository,
      ServiceHelper serviceHelper) {
    this.patientRepository = patientRepository;
    this.serviceHelper = serviceHelper;
  }

  @Override
  public Patient createPatient(RegisterPatientRequest request) {
    Clinic clinic = this.serviceHelper.getInjectableClinicFromAuthentication(null);
    Patient existingPatient = this.patientRepository.searchByClinicCodeAndEmailAddress(clinic.getCode(), request.getEmailAddress());
    if (Objects.nonNull(existingPatient)) {
      throw new XAdminBadRequestException("this user has existed");
    }
    Patient patient = PatientMapper.INSTANCE.createFrom(request);
    patient.setClinic(clinic);
    return this.patientRepository.save(patient);
  }

  @Override
  public Page<Patient> getPatients(PatientFilter patientFilter) {
    Clinic clinic = this.serviceHelper.getInjectableClinicFromAuthentication(patientFilter.getClinicCode());
    patientFilter.setClinicCode(clinic.getCode());

    return this.patientRepository.searchByFilter(patientFilter);
  }

}
