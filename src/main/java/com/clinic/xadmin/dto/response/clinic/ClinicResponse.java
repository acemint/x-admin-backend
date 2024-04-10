package com.clinic.xadmin.dto.response.clinic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicResponse {

  private String code;
  private String name;
  private BigInteger commissionFee;
  private BigInteger sittingFee;
  private BigInteger medicalItemFee;
  private LocalDateTime subscriptionValidFrom;
  private LocalDateTime subscriptionValidTo;
  private Integer subscriptionTier;

}
