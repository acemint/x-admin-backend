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

  @Column(name = "satu_sehat_patient_reference_id")
  private String satuSehatPatientReferenceId;

  @Column(name = "satu_sehat_practitioner_reference_id")
  private String satuSehatPractitionerReferenceId;

  @Column(name = "clinic_username", nullable = false, unique = true)
  private String clinicUsername;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "nik")
  private String nik;

  @Column(name = "email_address", nullable = false)
  private String emailAddress;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "address")
  private String address;

  @Column(name = "gender")
  private String gender;

  @Column(name = "age")
  private Integer age;

  @Column(name = "role", nullable = false)
  private String role;

  @Column(name = "status", nullable = false)
  private String status;

  @Column(name = "password")
  private String password;

  @Column(name = "practitioner_type")
  private String practitionerType;

  @Column(name = "practitioner_salary")
  private BigDecimal practitionerSalary;

  @Column(name = "practitioner_number")
  private String practitionerNumber;

  @Column(name = "practitioner_practice_license")
  private String practitionerPracticeLicense;

  @Column(name = "practitioner_tax_percentage")
  private BigDecimal practitionerTaxPercentage;

}
