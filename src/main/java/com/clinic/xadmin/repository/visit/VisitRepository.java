package com.clinic.xadmin.repository.visit;

import com.clinic.xadmin.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VisitRepository extends JpaRepository<Visit, String>, VisitCustomRepository {

  @Query(value = "SELECT CONCAT('VIS-', nextval('visit_sequence'))", nativeQuery = true)
  String getNextCode();

}
