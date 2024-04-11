package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xa_patient")
@FieldNameConstants
public class Patient extends BaseEntity {

  @ManyToOne
  @JoinColumn(name ="clinic_id", nullable = false)
  private Clinic clinic;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "age", nullable = false)
  private Integer age;

  @Column(name = "gender", nullable = false)
  private String gender;

  @Column(name = "email_address", nullable = false)
  private String emailAddress;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

}
