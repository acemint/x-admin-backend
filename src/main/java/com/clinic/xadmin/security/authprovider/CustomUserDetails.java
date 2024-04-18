package com.clinic.xadmin.security.authprovider;

import com.clinic.xadmin.entity.Member;
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

  private Member member;

  public Member getMember() {
    return this.member;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Stream.of(Optional.ofNullable(this.member.getRole())
            .map(rs -> rs.split("::"))
            .orElse(new String[] {}))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return this.member.getPassword();
  }

  @Override
  public String getUsername() {
    return this.member.getClinicUsername();
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
