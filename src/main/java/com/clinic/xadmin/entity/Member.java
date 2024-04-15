package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@FieldNameConstants
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xa_member")
public class Member extends BaseEntity {

  @ManyToOne
  @JoinColumn(name ="clinic_id")
  private Clinic clinic;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "satu_sehat_patient_reference_id", nullable = false)
  private String satuSehatPatientReferenceId;

  @Column(name = "satu_sehat_practitioner_reference_id", nullable = false)
  private String satuSehatPractitionerReferenceId;

  @Column(name = "clinic_username", nullable = false, unique = true)
  private String clinicUsername;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email_address", nullable = false)
  private String emailAddress;

  @Column(name = "nik", nullable = false)
  private String nik;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "gender", nullable = false)
  private String gender;

  @Column(name = "age", nullable = false)
  private Integer age;

  @Column(name = "type")
  private String type;

  @Column(name = "role", nullable = false)
  private String role;

  @Column(name = "status", nullable = false)
  private String status;

  @Column(name = "password")
  private String password;

  @Column(name = "salary")
  private BigDecimal salary;

  @Column(name = "tax_percentage")
  private BigDecimal taxPercentage;

}
