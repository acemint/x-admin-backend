package com.clinic.xadmin.repository.patient;

import com.clinic.xadmin.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository extends JpaRepository<Patient, String>, PatientCustomRepository {

  @Query(value = "SELECT CONCAT('PAT-', nextval('patient_sequence'))", nativeQuery = true)
  String getNextCode();

}
