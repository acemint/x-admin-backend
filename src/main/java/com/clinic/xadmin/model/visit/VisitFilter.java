package com.clinic.xadmin.model.visit;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@Builder
public class VisitFilter {

  private String clinicCode;

  private String status;

  @Builder.Default
  private Pageable pageable = Pageable.ofSize(10);

}
