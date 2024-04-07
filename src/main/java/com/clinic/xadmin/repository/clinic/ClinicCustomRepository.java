package com.clinic.xadmin.repository.clinic;


import com.clinic.xadmin.entity.Clinic;

public interface ClinicCustomRepository {

  Clinic searchByCode(String code);

}
