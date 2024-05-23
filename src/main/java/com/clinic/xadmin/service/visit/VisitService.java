package com.clinic.xadmin.service.visit;

import com.clinic.xadmin.dto.request.visit.CreateVisitRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Visit;

public interface VisitService {

  Visit createVisit(Clinic clinic, CreateVisitRequest createVisitRequest);

}
