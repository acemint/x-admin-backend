package com.clinic.xadmin.dto.response.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {

  private String code;
  private String firstName;
  private String lastName;
  private Integer age;
  private String gender;
  private String emailAddress;
  private String address;
  private String phoneNumber;

}
