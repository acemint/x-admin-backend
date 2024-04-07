package com.clinic.xadmin.model.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPatientData {

  private String firstName;
  private String lastName;
  private int age;
  private String gender;
  private String emailAddress;
  private String address;
  private String phoneNumber;
  private String clinicCode;

}
