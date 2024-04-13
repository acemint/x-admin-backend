package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.model.patient.PatientFilter;
import com.clinic.xadmin.model.patient.RegisterPatientData;
import org.springframework.data.domain.Page;

public interface PatientService {

  //  TODO: Would like to change this such that it hits the API from SatuSeaht
  @Deprecated
  Patient createPatient(RegisterPatientData registerPatientData);

//  TODO: Would like to change this such that it hits the filter from SatuSeaht
  @Deprecated
  Page<Patient> getPatients(PatientFilter patientFilter);

  // TODO: Might change, as the data fetched are actually incomplete
  Patient getPatient(Clinic clinic, String nik);

}
