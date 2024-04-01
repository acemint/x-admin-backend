package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.model.patient.PatientFilter;
import org.springframework.data.domain.Page;

public interface PatientService {

  Page<Patient> getPatients(PatientFilter patientFilter);

}
