package com.clinic.xadmin.service.helper;

import com.clinic.xadmin.entity.Clinic;
import jakarta.annotation.Nullable;

public interface ServiceHelper {

  @Nullable
  Clinic getClinicFromAuthentication();

}
