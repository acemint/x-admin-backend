package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Patient;
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
  public Page<Patient> getPatients(PatientFilter patientFilter) {
    Clinic clinic = this.serviceHelper.getClinicFromAuthentication();
    if (Objects.nonNull(clinic)) {
      patientFilter.setClinicId(clinic.getId());
    }
    return this.patientRepository.findByFilter(patientFilter);
  }

}
