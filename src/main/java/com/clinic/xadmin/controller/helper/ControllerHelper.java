package com.clinic.xadmin.controller.helper;

import com.clinic.xadmin.entity.Clinic;
import jakarta.annotation.Nonnull;

public interface ControllerHelper {

  /*
    This method will get Clinic ID from Authentication.
    By default, this will return the clinic from the authenticated General User Roles and throw error if the General User Roles.
    However, as there are use-cases where Firefighter Roles does not have clinic ID, they can inject specific clinic id if they want to.
  */
  @Nonnull
  Clinic getClinicScope(String clinicCode);


}
