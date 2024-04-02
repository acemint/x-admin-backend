package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xa_item")
public class Item extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="clinic_id")
  private Clinic clinic;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "quantity")
  private BigDecimal quantity;

  @Column(name = "status")
  private String status;

  @Column(name = "unit_of_measurement")
  private String unitOfMeasurement;

  @Column(name = "expiry_date")
  private LocalDate expiryDate;

}
