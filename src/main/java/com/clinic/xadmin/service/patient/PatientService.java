package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.entity.ClinicSatuSehatCredential;
import com.clinic.xadmin.model.patient.SatuSehatPatientFilter;

public interface PatientService {

  String getPatientFromSatuSehat(SatuSehatPatientFilter filter);

}
