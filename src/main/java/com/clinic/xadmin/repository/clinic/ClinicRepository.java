package com.clinic.xadmin.repository.clinic;

import com.clinic.xadmin.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, String>,
    ClinicCustomRepository {

}
