package com.clinic.xadmin.repository.patient;

import com.clinic.xadmin.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String>, PatientCustomRepository {

}
