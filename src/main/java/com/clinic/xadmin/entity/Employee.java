package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@FieldNameConstants
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xa_employee")
public class Employee extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="clinic_id", nullable = false)
  private Clinic clinic;

  @Column(name = "code")
  private String code;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email_address")
  private String emailAddress;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "password")
  private String password;

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
