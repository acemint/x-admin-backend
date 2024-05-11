package com.clinic.xadmin.model.clinic;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;


@Builder
@Data
public class ClinicFilter {

  private String keyword;
  private Pageable pageable;

}
