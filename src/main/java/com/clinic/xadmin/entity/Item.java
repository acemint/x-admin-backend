package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "xa_item")
public class Item {

  @Id
  @Column(name = "id")
  private String id;

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
