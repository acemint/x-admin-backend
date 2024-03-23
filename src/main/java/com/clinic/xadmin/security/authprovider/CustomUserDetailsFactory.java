package com.clinic.xadmin.security.authprovider;

import com.clinic.xadmin.entity.Employee;


public class CustomUserDetailsFactory {

  public static CustomUserDetails createFrom(Employee employee) {
    return CustomUserDetails.builder()
        .employee(employee)
        .build();
  }

}
