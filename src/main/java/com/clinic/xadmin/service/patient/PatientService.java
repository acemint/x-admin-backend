package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.model.patient.PatientFilter;
import com.clinic.xadmin.model.patient.RegisterPatientData;
import org.springframework.data.domain.Page;

public interface PatientService {

  Patient createPatient(RegisterPatientData registerPatientData);

  Page<Patient> getPatients(PatientFilter patientFilter);

}
