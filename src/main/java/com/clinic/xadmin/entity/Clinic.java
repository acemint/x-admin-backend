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

  @Column(name = "commission_fee")
  private BigDecimal commissionFee;

  @Column(name = "sitting_fee")
  private BigDecimal sittingFee;

  @Column(name = "medical_item_fee")
  private BigDecimal medicalItemFee;

  @Column(name = "subscription_valid_from")
  private LocalDateTime subscriptionValidFrom;

  @Column(name = "subscription_valid_to")
  private LocalDateTime subscriptionValidTo;

  @Column(name = "subscription_tier")
  private int subscriptionTier;

}
