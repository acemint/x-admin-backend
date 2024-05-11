package com.clinic.xadmin.repository.clinic;


import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.model.clinic.ClinicFilter;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface ClinicCustomRepository {

  Clinic searchByName(String name);
  Clinic searchByCode(String code);
  Page<Clinic> searchByFilter(ClinicFilter clinicFilter);

}
