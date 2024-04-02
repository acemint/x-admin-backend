package com.clinic.xadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "xa_patient")
@FieldNameConstants
public class Patient extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="clinic_id", nullable = false)
  private Clinic clinic;

  @Column(name = "code")
  private String code;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "age")
  private Integer age;

  @Column(name = "gender")
  private String gender;

  @Column(name = "email_address")
  private String emailAddress;

  @Column(name = "address")
  private String address;

  @Column(name = "phone_number")
  private String phoneNumber;

  @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
  private Set<Visit> visits;

}
