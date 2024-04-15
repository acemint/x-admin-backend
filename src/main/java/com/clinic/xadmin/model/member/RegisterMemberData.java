package com.clinic.xadmin.model.member;

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
public class RegisterMemberData {

  private String firstName;
  private String lastName;
  private int age;
  private String gender;
  private String emailAddress;
  private String status;
  private String role;
  private String type;
  private String address;
  private String phoneNumber;
  private String password;
  private String clinicCode;

}
