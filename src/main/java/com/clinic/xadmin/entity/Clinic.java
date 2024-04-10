package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xa_clinic")
public class Clinic extends BaseEntity {

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "commission_fee")
  private BigInteger commissionFee;

  @Column(name = "sitting_fee")
  private BigInteger sittingFee;

  @Column(name = "medical_item_fee")
  private BigInteger medicalItemFee;

  @Column(name = "subscription_valid_from")
  private LocalDateTime subscriptionValidFrom;

  @Column(name = "subscription_valid_to")
  private LocalDateTime subscriptionValidTo;

  @Column(name = "subscription_tier")
  private Integer subscriptionTier;

}
