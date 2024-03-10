package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "ms_employee")
public class Employee {

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

  @Column(name = "type")
  private String type;

  @Column(name = "role")
  private String role;

  @Column(name = "status")
  private String status;

  @Column(name = "salary")
  private BigDecimal salary;

  @Column(name = "tax_percentage")
  private BigDecimal taxPercentage;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private Set<Attendance> attendances;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private Set<Visit> visits;

}
