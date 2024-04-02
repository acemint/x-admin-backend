package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xa_clinic")
public class Clinic extends BaseEntity {

  @Column(name = "code")
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

  @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY)
  private Set<Employee> employees;

  @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY)
  private Set<Item> items;

  @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY)
  private Set<Patient> patients;

  @OneToMany(mappedBy = "clinic", fetch = FetchType.LAZY)
  private Set<Treatment> treatments;

}
