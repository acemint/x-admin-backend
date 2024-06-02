package com.clinic.xadmin.repository.visit;


import com.clinic.xadmin.entity.Visit;
import com.clinic.xadmin.model.visit.VisitFilter;
import org.springframework.data.domain.Page;

public interface VisitCustomRepository {

    Page<Visit> searchByFilter(VisitFilter filter);

}
