package com.clinic.xadmin.security.authprovider;

import com.clinic.xadmin.entity.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CustomUserDetailsFactory {

  public static CustomUserDetails createFrom(Employee employee) {
    Set<GrantedAuthority> grantedAuthorities = Stream.of(Optional.ofNullable(employee.getRole())
        .map(rs -> rs.split("::"))
        .orElse(new String[] {}))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());

    return CustomUserDetails.builder()
        .id(employee.getId())
        .email(employee.getEmailAddress())
        .password(employee.getPassword())
        .firstName(employee.getFirstName())
        .grantedAuthorities(grantedAuthorities)
        .lastName(employee.getLastName())
        .build();
  }

}
