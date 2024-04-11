package com.clinic.xadmin.repository.clinic;

import com.clinic.xadmin.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClinicRepository extends JpaRepository<Clinic, String>,
    ClinicCustomRepository {

  @Query(value = "SELECT CONCAT('CLC-', nextval('clinic_sequence'))", nativeQuery = true)
  String getNextCode();

}
