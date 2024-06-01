package com.clinic.xadmin.service.patient;

import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.model.patient.SatuSehatPatientFilter;

public interface PatientService {

  @Deprecated
  String getPatientFromSatuSehat(SatuSehatPatientFilter filter);

  String getOrCreateSatuSehatPatient(Member member);

}
