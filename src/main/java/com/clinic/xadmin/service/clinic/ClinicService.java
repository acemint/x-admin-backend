package com.clinic.xadmin.service.clinic;

import com.clinic.xadmin.dto.request.clinic.RegisterClinicRequest;
import com.clinic.xadmin.entity.Clinic;

public interface ClinicService {

  Clinic createClinic(RegisterClinicRequest request);

}
