package com.clinic.xadmin.service.clinic;

import com.clinic.xadmin.dto.request.clinic.RegisterClinicRequest;
import com.clinic.xadmin.dto.request.clinic.UpdateClinicRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.model.clinic.ClinicFilter;
import org.springframework.data.domain.Page;

public interface ClinicService {

  Clinic createClinic(RegisterClinicRequest request);

  Clinic editClinic(String clinicCode, UpdateClinicRequest request);

  Clinic getClinic(String clinicCode);

  Page<Clinic> getClinics(ClinicFilter clinicFilter);

}
