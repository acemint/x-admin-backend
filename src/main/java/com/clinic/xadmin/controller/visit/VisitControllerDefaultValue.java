package com.clinic.xadmin.controller.visit;

import com.clinic.xadmin.entity.Visit;

public interface VisitControllerDefaultValue {

  String DEFAULT_VISIT_STATUS = "PLANNED";
  String DEFAULT_PAGE_NUMBER = "0";
  String DEFAULT_PAGE_SIZE = "10";
  String DEFAULT_SORT_ORDER = Visit.Fields.startTime;
  String DEFAULT_SORT_DIRECTION = "DESC";

}
