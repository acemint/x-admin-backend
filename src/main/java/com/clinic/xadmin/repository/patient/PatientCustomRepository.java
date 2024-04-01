package com.clinic.xadmin.repository.patient;


import com.clinic.xadmin.entity.Patient;
import com.clinic.xadmin.model.patient.PatientFilter;
import org.springframework.data.domain.Page;

public interface PatientCustomRepository {

  Page<Patient> findByFilter(PatientFilter filter);

}
