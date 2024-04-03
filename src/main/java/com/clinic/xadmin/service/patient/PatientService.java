package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.dto.request.patient.RegisterPatientRequest;
import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.model.patient.PatientFilter;
import org.springframework.data.domain.Page;

public interface PatientService {

  Patient createPatient(RegisterPatientRequest request);

  Page<Patient> getPatients(PatientFilter patientFilter);

}
