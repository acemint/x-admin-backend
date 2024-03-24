package com.clinic.xadmin.security.authprovider;

import com.clinic.xadmin.entity.Employee;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public class CustomUserDetails implements UserDetails {

  private Employee employee;

  public Employee getEmployee() {
    return this.employee;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Stream.of(Optional.ofNullable(this.employee.getRole())
            .map(rs -> rs.split("::"))
            .orElse(new String[] {}))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return this.employee.getPassword();
  }

  @Override
  public String getUsername() {
    return this.employee.getEmailAddress();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
