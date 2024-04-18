package com.clinic.xadmin.repository.member;


import com.clinic.xadmin.entity.Member;
import com.clinic.xadmin.model.member.MemberFilter;
import org.springframework.data.domain.Page;

public interface MemberCustomRepository {

  Member searchByUsername(String username);
  Member searchByClinicCodeAndEmailAddress(String clinicCode, String emailAddress);
  Page<Member> searchByFilter(MemberFilter filter);

}
