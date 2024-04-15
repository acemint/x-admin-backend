package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.model.patient.SatuSehatPatientFilter;

public interface PatientService {

  String getPatientFromSatuSehat(Clinic clinic, SatuSehatPatientFilter filter);

}
