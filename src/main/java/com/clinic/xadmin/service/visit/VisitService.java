package com.clinic.xadmin.service.visit;

import com.clinic.xadmin.dto.request.visit.CreateVisitRequest;
import com.clinic.xadmin.entity.Clinic;
import com.clinic.xadmin.entity.Visit;
import com.clinic.xadmin.model.visit.VisitFilter;
import org.springframework.data.domain.Page;

public interface VisitService {

  Visit createVisit(Clinic clinic, CreateVisitRequest createVisitRequest);

  Page<Visit> get(VisitFilter visitFilter);


}
