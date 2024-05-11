package com.clinic.xadmin.controller.clinic;

import com.clinic.xadmin.repository.clinic.ClinicCustomRepositoryImpl;

public interface ClinicControllerDefaultValue {
  
  String DEFAULT_PAGE_NUMBER = "0";
  String DEFAULT_PAGE_SIZE = "10";
  String DEFAULT_SORT_BY = ClinicCustomRepositoryImpl.SORT_BY_CREATED_DATE;
  String DEFAULT_SORT_DIRECTION = "DESC";

}
