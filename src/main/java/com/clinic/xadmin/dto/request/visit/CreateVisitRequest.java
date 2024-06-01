package com.clinic.xadmin.dto.request.visit;

import com.clinic.xadmin.validator.annotation.ValidFutureEpochAsLong;
import com.clinic.xadmin.validator.annotation.visit.ValidStartAndEndTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@ValidStartAndEndTime
public class CreateVisitRequest {

  @NotNull
  private String practitionerCode;

  @NotNull
  private String patientCode;

  @NotNull
  @ValidFutureEpochAsLong
  private Long startTime;

  @NotNull
  @ValidFutureEpochAsLong
  private Long endTime;

}
