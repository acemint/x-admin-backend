package com.clinic.xadmin.security.authprovider;

import com.clinic.xadmin.entity.Member;


public class CustomUserDetailsFactory {

  public static CustomUserDetails createFrom(Member member) {
    return CustomUserDetails.builder()
        .member(member)
        .build();
  }

}
