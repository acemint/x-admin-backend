package com.clinic.xadmin.repository.patient;


import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.model.patient.PatientFilter;
import org.springframework.data.domain.Page;

public interface PatientCustomRepository {

  Patient findByClinicIdAndEmailAddress(String clinicId, String emailAddress);
  Page<Patient> findByFilter(PatientFilter filter);

}
