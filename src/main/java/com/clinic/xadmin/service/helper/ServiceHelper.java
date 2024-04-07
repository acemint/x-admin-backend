package com.clinic.xadmin.service.helper;

import com.clinic.xadmin.entity.Clinic;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface ServiceHelper {

  /*
    This method will get Clinic ID from Authentication.
    By default, this will return the clinic from the authenticated General User Roles and throw error if the General User Roles.
    However, as there are use-cases where Firefighter Roles does not have clinic ID, they can inject specific clinic id if they want to.
  */
  @Nonnull Clinic getInjectableClinicFromAuthentication(@Nullable String clinicCode);


}
