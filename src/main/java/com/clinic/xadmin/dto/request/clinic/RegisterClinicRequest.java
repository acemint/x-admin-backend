package com.clinic.xadmin.dto.request.clinic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterClinicRequest {

  @NotNull
  private String name;

  @NotNull
  private String satuSehatOrganizationKey;

  @NotNull
  private String satuSehatClientKey;

  @NotNull
  private String satuSehatSecretKey;

  private LocalDateTime subscriptionValidFrom;

  @Future
  private LocalDateTime subscriptionValidTo;

  @PositiveOrZero
  private Integer subscriptionTier = 0;

  @PositiveOrZero
  private BigInteger commissionFee = BigInteger.ZERO;

  @PositiveOrZero
  private BigInteger sittingFee = BigInteger.ZERO;

  @PositiveOrZero
  private BigInteger medicalItemFee = BigInteger.ZERO;

}
