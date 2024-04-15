package com.clinic.xadmin.security.authprovider;

import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service(value = CustomUserDetailsServiceImpl.BEAN_NAME)
public class CustomUserDetailsServiceImpl implements UserDetailsService {

  public static final String BEAN_NAME = "CustomUserDetailsServiceImpl";

  private final MemberRepository memberRepository;

  @Autowired
  public CustomUserDetailsServiceImpl(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = this.memberRepository.searchByUsername(username);
    if (Objects.isNull(member)) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    return CustomUserDetailsFactory.createFrom(member);
  }
}
